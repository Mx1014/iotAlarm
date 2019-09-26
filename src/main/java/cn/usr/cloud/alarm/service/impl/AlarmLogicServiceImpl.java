package cn.usr.cloud.alarm.service.impl;

import cn.usr.cloud.alarm.dto.AlarmInfoDTO;
import cn.usr.cloud.alarm.entity.*;
import cn.usr.cloud.alarm.mapper.one.AlarmMapper;
import cn.usr.cloud.alarm.mapper.one.DataPointMapper;
import cn.usr.cloud.alarm.ignite.DeviceStatusMapper;
import cn.usr.cloud.alarm.service.*;
import cn.usr.cloud.alarm.util.EmptyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.StringTokenizer;

import static cn.usr.cloud.alarm.constant.ConstantCacheKey.CACHE_ALARM_DEVICE;

@Service
@Slf4j
public class AlarmLogicServiceImpl implements AlarmLogicService {
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Autowired
    AlarmMapper alarmDao;

    @Autowired
    DataPointMapper dataPointDao;

    @Autowired
    UserService userService;

    @Autowired
    DeviceService deviceService;

    @Autowired
    SlaveService slaveService;

    @Autowired
    DataPointService dataPointService;

    @Autowired
    DeviceStatusMapper deviceStatusMapper;

    @Override
    public Boolean hasTriggered(String deviceId, Integer dataPointId, Integer triggerId) {
        boolean has = redisTemplate.hasKey(getRedisKeyForAlarmState(deviceId, dataPointId, triggerId));
        return has;
    }

    @Override
    public void setTriggered(String deviceId, Integer dataPointId, Integer triggerId, Double value) {
        redisTemplate.opsForValue().set(getRedisKeyForAlarmState(deviceId, dataPointId, triggerId), value.toString());
        //设备还存在报警
        updateAlarmStatusForIgnite(deviceId);

    }

    @Override
    public void removeTriggered(String deviceId, Integer dataPointId, Integer triggerId) {
        redisTemplate.delete(getRedisKeyForAlarmState(deviceId,dataPointId, triggerId));

        updateAlarmStatusForIgnite(deviceId);
    }

    @Override
    public AlarmInfoDTO getAlarmInfo( String deviceId, Integer dataPointId) {

        DeviceInfo deviceInfo = deviceService.getDeviceByDeviceId(deviceId);
        if(EmptyUtil.isEmpty(deviceInfo)){
            log.warn("deviceInfo为null");
            return null;
        }

        //todo 权限体系
        UserInfo userInfo = userService.getUserInfo(deviceInfo.getOwnerUid());
        if(EmptyUtil.isEmpty(userInfo)){
            log.warn("userInfo为null");
            return null;
        }

        DataPointInfo dataPointInfo = dataPointService.getDataPointById(dataPointId);
        if(EmptyUtil.isEmpty(dataPointInfo)){
            log.warn("dataPointInfo为null");
            return null;
        }

        AlarmInfoDTO alarmInfoDTO = new AlarmInfoDTO();
        alarmInfoDTO.setDeviceInfo(deviceInfo);
        alarmInfoDTO.setUserInfo(userInfo);
        alarmInfoDTO.setDataPointInfo(dataPointInfo);
        return alarmInfoDTO;
    }

    /**
     * 此方法获取redis中某个数据点的某个触发器是否在报警状态的key
     * 并且兼容了模糊匹配的key，triggerId或 dataPointId 不确定是可以传null，则会生成以*代替该值的key
     * @param triggerId
     * @param dataPointId
     * @return
     */
    public String getRedisKeyForAlarmState(String deviceId, Integer dataPointId, Integer triggerId) {
        String dId = deviceId == null ? "*" : deviceId;
        String tId = triggerId == null ? "*" : triggerId.toString();
        String pId = dataPointId == null ? "*" : dataPointId.toString();
        return CACHE_ALARM_DEVICE + dId + ":" + pId + ":" + tId;
    }

    @Override
    public void updateAlarmStatusForIgnite(Set<String> keys) {
        keys.forEach(key -> {
            StringTokenizer token = new StringTokenizer(key, ":");
            token.nextElement();
            String deviceId = (String) token.nextElement();
            updateAlarmStatusForIgnite(deviceId);
        });
    }

    /**
     * 更新设备在Ignite中的报警状态
     * @param deviceId
     */
    private void updateAlarmStatusForIgnite(String deviceId){
        String key = getRedisKeyForAlarmState(deviceId, null, null);
        Set<String> tempKeys = redisTemplate.keys(key);
        DeviceStatus deviceStatus;
        if(EmptyUtil.isEmpty(tempKeys)){
            //设备已经没有报警
            deviceStatus = new DeviceStatus(deviceId, 0);
        }else{
            //设备还存在报警
            deviceStatus = new DeviceStatus(deviceId, 1);
        }
        setDeviceStatus(deviceStatus);
    }

    private void setDeviceStatus(DeviceStatus deviceStatus){
        DeviceStatus deviceStatus1 = deviceStatusMapper.getDeviceStatus(deviceStatus.getDeviceId());
        if(EmptyUtil.isEmpty(deviceStatus1)){
            deviceStatusMapper.insertDeviceStatus(deviceStatus);
        }else{
            deviceStatusMapper.updateDeviceStatus(deviceStatus);
        }
    }

}
