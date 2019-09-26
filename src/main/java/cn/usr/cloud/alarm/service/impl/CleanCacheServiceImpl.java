package cn.usr.cloud.alarm.service.impl;

import cn.usr.cloud.alarm.service.CleanCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;


import static cn.usr.cloud.alarm.constant.ConstantCacheKey.*;

/**
 * @Auther: Andy(李永强)
 * @Date: 2019/6/19 上午10:03
 * @Describe:
 */
@Service
@Slf4j
public class CleanCacheServiceImpl implements CleanCacheService {

    @Override
    @CacheEvict(value = CACHE_USER, key="#userId")
    public void cleanUser(String userId) {}

    @Override
    @CacheEvict(value = CACHE_TRIGGER, key="#dataPointId")
    public void cleanTrigger(Integer dataPointId) {}

    @Override
    @CacheEvict(value = CACHE_DATA_POINT, key="#dataPointId")
    public void cleanDataPoint(Integer dataPointId) {}

    @Override
    @CacheEvict(value = CACHE_DEVICE, key="#deviceNo")
    public void cleanDevice(String deviceNo) {}

    @Override
    @CacheEvict(value = CACHE_SLAVE, key="#deviceTemplateId")
    public void cleanSlave(Integer deviceTemplateId) {}

    @Override
    @CacheEvict(value = CACHE_CONTACTS, key="#triggerId")
    public void cleanContacts(Integer triggerId) {}

    @Override
    @CacheEvict(value = CACHE_LINKAGE, key="#triggerId")
    public void cleanLinkage(Integer triggerId) {}

    @Override
    @CacheEvict(value = CACHE_DATA_POINT_REL, key="#dataPointTemplateId")
    public void cleanDataPointRel(Integer dataPointTemplateId) {}


}
