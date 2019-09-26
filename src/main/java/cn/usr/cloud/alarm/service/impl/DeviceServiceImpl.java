package cn.usr.cloud.alarm.service.impl;

import cn.usr.cloud.alarm.constant.ConstantCacheKey;
import cn.usr.cloud.alarm.entity.DeviceInfo;
import cn.usr.cloud.alarm.mapper.one.DeviceMapper;
import cn.usr.cloud.alarm.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Created by pi on 2018/10/10.
 */
@Slf4j
@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    DeviceMapper deviceMapper;

    @Override
    @Cacheable(value = ConstantCacheKey.CACHE_DEVICE, key = "#deviceId")
    public DeviceInfo getDeviceByDeviceId(String deviceId) {
        try {
            return deviceMapper.getDeviceByDeviceId(deviceId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("【异常】获取设备信息失败 - 设备id为：{}", deviceId);
            return null;
        }
    }
}
