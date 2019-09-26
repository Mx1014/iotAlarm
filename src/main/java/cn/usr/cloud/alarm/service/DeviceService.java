package cn.usr.cloud.alarm.service;


import cn.usr.cloud.alarm.entity.DeviceInfo;

/**
 * Created by pi on 2018/10/10.
 */
public interface DeviceService {
    DeviceInfo getDeviceByDeviceId(String deviceId);
}
