package cn.usr.cloud.alarm.service;


import cn.usr.cloud.alarm.dto.AlarmInfoDTO;

import java.util.Set;

public interface AlarmLogicService {
    Boolean hasTriggered(String deviceId, Integer dataPointId, Integer triggerId);
    void setTriggered(String deviceId, Integer dataPointId, Integer triggerId, Double value);
    void removeTriggered(String deviceId, Integer dataPointId, Integer triggerId);

    AlarmInfoDTO getAlarmInfo(String deviceId, Integer dataPointId);

    String getRedisKeyForAlarmState(String deviceId, Integer dataPointId, Integer triggerId);

    /**
     * 在缓存中更新设备报警状态
     * @param keys
     */
    void updateAlarmStatusForIgnite(Set<String> keys);
}
