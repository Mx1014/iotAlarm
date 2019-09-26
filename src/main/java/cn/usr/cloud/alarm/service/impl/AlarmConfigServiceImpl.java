package cn.usr.cloud.alarm.service.impl;

import cn.usr.cloud.alarm.constant.ConstanControlTargetType;
import cn.usr.cloud.alarm.constant.ConstantCacheKey;
import cn.usr.cloud.alarm.dto.AlarmContactsDTO;
import cn.usr.cloud.alarm.dto.TargetDataPointDTO;
import cn.usr.cloud.alarm.entity.DataPointInfo;
import cn.usr.cloud.alarm.entity.Linkage;
import cn.usr.cloud.alarm.entity.Trigger;
import cn.usr.cloud.alarm.mapper.one.AlarmContactsMapper;
import cn.usr.cloud.alarm.mapper.one.AlarmMapper;
import cn.usr.cloud.alarm.mapper.one.DataPointMapper;
import cn.usr.cloud.alarm.mapper.one.TriggerAlarmMapper;
import cn.usr.cloud.alarm.service.AlarmConfigService;
import cn.usr.cloud.alarm.service.DataPointService;
import cn.usr.cloud.alarm.service.LinkageControlService;
import cn.usr.cloud.alarm.util.EmptyUtil;
import cn.usr.cloud.alarm.util.ScriptHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlarmConfigServiceImpl implements AlarmConfigService {
    @Autowired
    AlarmMapper alarmMapper;

    @Autowired
    DataPointMapper dataPointMapper;

    @Autowired
    DataPointService dataPointService;

    @Autowired
    AlarmContactsMapper alarmContactsMapper;

    @Autowired
    TriggerAlarmMapper triggerAlarmMapper;

    @Autowired
    LinkageControlService linkageControlService;

    @Override
    @Cacheable(value = ConstantCacheKey.CACHE_TRIGGER, key = "#dataPointId", cacheManager = "cacheManagerForExpire")
    public List<Trigger> getTriggerByDataPointId(Integer dataPointId) {
        return triggerAlarmMapper.getTriggerByDataPointId(dataPointId);
    }

    @Override
    @Cacheable(value = ConstantCacheKey.CACHE_CONTACTS, key = "#triggerId")
    public List<AlarmContactsDTO> getContactListByTriggerId(Integer triggerId) {
        return alarmContactsMapper.getAlarmContactsByTriggerId(triggerId);
    }

    @Override
    public Linkage getLinkageByTriggerId(Integer triggerId, String deviceId) {
        Linkage linkage = linkageControlService.getLinkageByTriggerId(triggerId);
        if (linkage == null) {
            return null;
        }
        linkage.setDeviceId(deviceId);
        if(linkage.getControlTargetType() == ConstanControlTargetType.CONTROL_SELF){
            //如果是控制自己那么deviceId需要手动设置上
            linkage.setDeviceId(deviceId);
            //如果控制自己，在linkage中存储的是数据点的模板id需要根据模板id查询真实id
            Integer linkageDataPointId = dataPointMapper.getDataPointRelIdByDeviceIdDataPointTemplateId(deviceId, linkage.getDataPointTemplateId());
            linkage.setDataPointId(linkageDataPointId);

            //按模板数据点联动
//            List<TargetDataPointDTO> targetDataPointList = dataPointService.getDataPointListByDataPointTemplateId(linkage.getDataPointTemplateId());
//            linkage.setTargetDataPointList(targetDataPointList);
        }else{
            //按单个设备数据点联动

            /*TargetDataPointDTO targetDataPoint = new TargetDataPointDTO(linkage.getDeviceId(), linkage.getDataPointTemplateId());
            List<TargetDataPointDTO> targetDataPointList = new ArrayList<>();
            targetDataPointList.add(targetDataPoint);*/
            linkage.setDataPointId(linkage.getDataPointTemplateId());
        }
        if(linkage.getDataPointId() == null) return null;

        DataPointInfo dataPointInfo = dataPointService.getDataPointById(linkage.getDataPointId());
        if(EmptyUtil.isNotEmpty(dataPointInfo.getReverseFormula())){//如果有反向公式需要进行公式计算
            String sendData = ScriptHandler.process(linkage.getSendData(), dataPointInfo.getReverseFormula());
            linkage.setSendData(sendData);
        }
        return linkage;
    }

}
