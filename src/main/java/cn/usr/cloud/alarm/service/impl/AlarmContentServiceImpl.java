package cn.usr.cloud.alarm.service.impl;

import cn.usr.cloud.alarm.dto.AlarmInfoDTO;
import cn.usr.cloud.alarm.entity.*;
import cn.usr.cloud.alarm.mapper.one.*;
import cn.usr.cloud.alarm.service.AlarmContentService;
import cn.usr.cloud.alarm.service.SlaveService;
import cn.usr.cloud.alarm.util.AlarmUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.vertx.core.json.JsonObject;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.usr.cloud.alarm.util.AlarmUtil.milliSecondForDate;


@Service
public class AlarmContentServiceImpl implements AlarmContentService {
    @Autowired
    Configuration configuration;

    @Autowired
    DataPointMapper dataPointMapper;

    @Autowired
    DeviceMapper deviceMapper;

    @Autowired
    DeviceAlarmMapper deviceAlarmMapper;

    @Autowired
    SlaveMapper deviceSlaveAlarmMapper;

    @Autowired
    SlaveService slaveService;

    @Autowired
    DataPointAlarmMapper dataPointAlarmMapper;

    @Autowired
    UserAlarmMapper userAlarmDao;

    private SimpleDateFormat format1 = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");

    @Override
    public String getSmsContent(AlarmInfoDTO alarmInfoDTO) {
        String alarmState = alarmInfoDTO.isAlarmState() ? "触发报警" : "恢复正常";
        String deviceName = alarmInfoDTO.getDeviceInfo().getName();
        String dataPointName = alarmInfoDTO.getDataPointInfo().getName();

        deviceName = deviceName == null ? "" : deviceName;
        dataPointName = dataPointName == null ? "" : dataPointName;

        if(deviceName.length() > 38){
            deviceName = deviceName.substring(0, 38);
        }

        if(dataPointName.length() > 28){
            dataPointName = dataPointName.substring(0, 28);
        }
        JsonObject json = new JsonObject()
                .put("DEVNAME", deviceName)
                .put("DATAPOINTNAME", dataPointName)
                .put("TRIGGERCONDITION", alarmState)
                .put("NOWVALUE", alarmInfoDTO.getTrigger().getInputValue());

        return json.toString();
    }

    /**
     * 组织有人物联网短信参数
     *
     * @param alarmInfoDTO
     * @return
     */
    public String getVmsContent(AlarmInfoDTO alarmInfoDTO) {

        final String deviceName = alarmInfoDTO.getDeviceInfo().getName();
        final String slaveName = alarmInfoDTO.getSlaveInfo().getSlaveName();
        final String dataPointName = alarmInfoDTO.getDataPointInfo().getName();
        final String nowValue = alarmInfoDTO.getValue().toString();
        final String alarmState = alarmInfoDTO.isAlarmState() ? "触发报警" : "恢复正常";

        JsonObject vmsParam = new JsonObject()
                .put("DEVNAME",deviceName)
                .put("SLAVENAME",slaveName)
                .put("DATAPOINTNAME",dataPointName)
                .put("TRIGGERCONDITION",alarmState)
                .put("NOWVALUE",nowValue);

        return vmsParam.toString();
    }

    @Override
    public String getEmailContent(AlarmInfoDTO alarmInfoDTO) {
        DeviceInfo deviceInfo = alarmInfoDTO.getDeviceInfo();
        Trigger trigger = alarmInfoDTO.getTrigger();
        UserInfo userInfo = alarmInfoDTO.getUserInfo();
        DataPointInfo dataPointInfo = alarmInfoDTO.getDataPointInfo();

        final SlaveInfo slaveDevice = slaveService.getSlaveListBytemplateId(deviceInfo.getTemplateId())
                .stream().filter(slaveInfo -> slaveInfo.getSlaveIndex().equals(trigger.getSlaveIndex()))
                .findFirst().get();

        String alarmMsg = getPlainContent(trigger, alarmInfoDTO.isAlarmState());
        Map dataMap = new HashMap();

        dataMap.put("userObject", userInfo);
        dataMap.put("deviceObject", deviceInfo);
        dataMap.put("slaveObject", slaveDevice);
        dataMap.put("dataPointObject", dataPointInfo);
        dataMap.put("triggerObject", trigger);
        dataMap.put("alarmState", alarmInfoDTO.isAlarmState());
        dataMap.put("alarmMsg", alarmMsg);
        dataMap.put("alarmTime", format1.format(alarmInfoDTO.getAlarmTime().longValue() * 1000));

        return getStringFromTpl("email.ftl", dataMap);
    }

    @Override
    public List<WxMpTemplateData> getWeChatContent(AlarmInfoDTO alarmInfoDTO) {

        Trigger trigger = alarmInfoDTO.getTrigger();
        DeviceInfo deviceInfo = alarmInfoDTO.getDeviceInfo();
        DataPointInfo dataPointInfo = alarmInfoDTO.getDataPointInfo();

        List<WxMpTemplateData> dataList = new ArrayList<>();
        String title =  String.format("%s设备下的%s号从机, 数据点[%s]%s", deviceInfo.getName(), trigger.getSlaveIndex(), dataPointInfo.getName(), alarmInfoDTO.isAlarmState() ? "触发报警" : "恢复正常");
        dataList.add(new WxMpTemplateData("first", title, alarmInfoDTO.isAlarmState()?"#FF3333":"#2ca175"));

        dataList.add(new WxMpTemplateData("keyword1", deviceInfo.getName(), "#000000"));
        dataList.add(new WxMpTemplateData("keyword2", dataPointInfo.getName(), "#000000"));
        dataList.add(new WxMpTemplateData("keyword3", trigger.getInputValue().toString(), "#000000"));
        dataList.add(new WxMpTemplateData("keyword4", getPlainContent(trigger, alarmInfoDTO.isAlarmState()), "#000000"));

        dataList.add(new WxMpTemplateData("keyword5", milliSecondForDate(null), "#000000"));
        dataList.add(new WxMpTemplateData("remark", "详细信息请登陆后台系统查看", "#AAAAAA"));
        return dataList;
    }

    @Override
    public String getPlainContent(Trigger trigger, boolean alarmState) {
        Map dataMap = new HashMap();
        dataMap.put("trigger", trigger);
        String content = AlarmUtil.getTriggerCondition(trigger, alarmState);
        return content;
    }

    String getWeChatMsgTitle(String deviceName, String slaveIndex, String dataPointName, Trigger trigger, boolean alarmState) {
        return String.format("%s设备下的%s号从机, 数据点[%s]%s", deviceName, slaveIndex, dataPointName, alarmState ? "" : "");
    }

    String getStringFromTpl(String tplName, Map dataMap) {
        try {
            Template template = configuration.getTemplate(tplName);
            StringWriter stringWriter = new StringWriter();
            template.process(dataMap, stringWriter);
            return stringWriter.toString().trim();
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        return "freemarker error";
    }
}
