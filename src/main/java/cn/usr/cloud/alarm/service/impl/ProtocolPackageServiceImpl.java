package cn.usr.cloud.alarm.service.impl;

import cn.usr.cloud.alarm.dto.AlarmInfoDTO;
import cn.usr.cloud.alarm.dto.DataPointDTO;
import cn.usr.cloud.alarm.entity.DataPointInfo;
import cn.usr.cloud.alarm.entity.Linkage;
import cn.usr.cloud.alarm.entity.Trigger;
import cn.usr.cloud.alarm.mapper.one.DeviceMapper;
import cn.usr.cloud.alarm.service.DataPointService;
import cn.usr.cloud.alarm.service.ProtocolPackageService;
import cn.usr.cloud.alarm.util.AlarmUtil;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by pi on 2019-03-13.
 */
@Service
public class ProtocolPackageServiceImpl implements ProtocolPackageService {
    @Autowired
    DeviceMapper deviceDao;
    @Autowired
    DataPointService dataPointService;

    @Override
    public String frontenfAlarmPackage(AlarmInfoDTO alarmInfoDTO) {
        Trigger trigger = alarmInfoDTO.getTrigger();
        DataPointInfo dataPointInfo = alarmInfoDTO.getDataPointInfo();
        String alarmCondition = AlarmUtil.getTriggerCondition(trigger, alarmInfoDTO.isAlarmState());

        JsonObject res = new JsonObject();
        res.put("version", 0)
                .put("f", "alarm");


        JsonObject content = new JsonObject();
        content.put("deviceId", trigger.getDeviceId())
                .put("slaveIndex", trigger.getSlaveIndex());

        content.put("item", dataPointInfo.getItemId())
                .put("dataPointName", dataPointInfo.getName())
                .put("pointId", alarmInfoDTO.getDataPointRelId())
                .put("value", trigger.getInputValue().toString())
                .put("triggerName", trigger.getTriggerName())
                .put("alarmCondition", alarmCondition)
                .put("alarmState", alarmInfoDTO.isAlarmState() ? 1 : 0);

        // todo String deviceName = deviceDao.getDeviceNameByDeviceId(trigger.getDeviceId());

        content.put("deviceName", alarmInfoDTO.getDeviceInfo().getName())
                .put("time", "" + System.currentTimeMillis() / 1000L);

        JsonObject alarmValue = new JsonObject();

        if (null != trigger.getMax()) {
            alarmValue.put("max", trigger.getMax().toString());
        }
        if (null != trigger.getMin()) {
            alarmValue.put("min", trigger.getMin().toString());
        }
        alarmValue.put("boundary", trigger.getMin() != null ? trigger.getMin() : trigger.getMax());

        content.put("alarmValue", alarmValue);

        res.put("c", content);
        return res.toString();
    }

    @Override
    public String linkageDevicePackage(Linkage... linkages) {
        JsonArray dataPoints = new JsonArray();
        for(Linkage linkage : linkages){
            JsonObject dataPoint = new JsonObject();

            dataPoint.put("deviceId", linkage.getDeviceId());
            dataPoint.put("slaveIndex", linkage.getSlaveIndex());
            dataPoint.put("dataPointId", linkage.getDataPointId());
            if(linkage.isQuery()){
                dataPoint.put("operation", "query");
            }else{
                dataPoint.put("operation", "control");
                dataPoint.put("value", linkage.getSendData());
            }
            dataPoints.add(dataPoint);
        }
        JsonObject res = new JsonObject();
        res.put("dataPoints", dataPoints);
        return res.toString();
    }
}
