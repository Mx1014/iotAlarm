package cn.usr.cloud.alarm.service;


import cn.usr.cloud.alarm.dto.AlarmContactsDTO;
import cn.usr.cloud.alarm.entity.Linkage;
import cn.usr.cloud.alarm.entity.Trigger;

import java.util.List;


public interface AlarmConfigService {
    List<Trigger> getTriggerByDataPointId(Integer dataPointId);
    List<AlarmContactsDTO> getContactListByTriggerId(Integer triggerId);
    Linkage getLinkageByTriggerId(Integer triggerId, String deviceId);
}
