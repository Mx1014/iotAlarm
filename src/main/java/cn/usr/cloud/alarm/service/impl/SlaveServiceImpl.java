package cn.usr.cloud.alarm.service.impl;

import cn.usr.cloud.alarm.constant.ConstantCacheKey;
import cn.usr.cloud.alarm.entity.SlaveInfo;
import cn.usr.cloud.alarm.mapper.one.SlaveMapper;
import cn.usr.cloud.alarm.service.SlaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by pi on 2018/10/10.
 */
@Slf4j
@Service
public class SlaveServiceImpl implements SlaveService {

    @Autowired
    SlaveMapper slaveMapper;

    @Override
    @Cacheable(value = ConstantCacheKey.CACHE_SLAVE, key = "#deviceTemplateId")
    public List<SlaveInfo> getSlaveListBytemplateId(Integer deviceTemplateId) {
        try {
            return slaveMapper.getSlaveListBytemplateId(deviceTemplateId);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("【异常】获取从机列表 - 模板id为：{}", deviceTemplateId);
            return null;
        }
    }

}
