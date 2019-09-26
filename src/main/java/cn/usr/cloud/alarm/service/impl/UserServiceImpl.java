package cn.usr.cloud.alarm.service.impl;

import cn.usr.cloud.alarm.constant.ConstantCacheKey;
import cn.usr.cloud.alarm.entity.UserInfo;
import cn.usr.cloud.alarm.mapper.one.UserMapper;
import cn.usr.cloud.alarm.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Created by pi on 2018/10/10.
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    @Cacheable(value = ConstantCacheKey.CACHE_USER, key = "#userId")
    public UserInfo getUserInfo(long userId) {
        try {
            // 在缓存中获取用户信息
            return userMapper.getUserByUserId(userId);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("【异常】获取用户信息失败 - 用户id为：{}", userId);
            return null;
        }
    }
}
