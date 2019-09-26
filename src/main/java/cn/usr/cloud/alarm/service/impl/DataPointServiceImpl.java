package cn.usr.cloud.alarm.service.impl;

import cn.usr.cloud.alarm.constant.ConstantCacheKey;
import cn.usr.cloud.alarm.dto.TargetDataPointDTO;
import cn.usr.cloud.alarm.entity.DataPointInfo;
import cn.usr.cloud.alarm.entity.DataPointTemplate;
import cn.usr.cloud.alarm.mapper.one.DataPointMapper;
import cn.usr.cloud.alarm.service.DataPointService;
import cn.usr.cloud.alarm.util.EmptyUtil;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.usr.cloud.alarm.constant.ConstantCacheKey.CACHE_DATA_POINT_REL;

/**
 * Created by pi on 2018/10/10.
 */
@Slf4j
@Service
public class DataPointServiceImpl implements DataPointService {
    @Autowired
    DataPointMapper dataPointMapper;

    @Cacheable(value = ConstantCacheKey.CACHE_DATA_POINT, key = "#p0", cacheManager = "cacheManagerForExpire")
    public DataPointInfo getDataPointById(Integer dataPointId){

        DataPointInfo dataPoint = dataPointMapper.getDataPointInfoByDataPointId(dataPointId);
        if(EmptyUtil.isEmpty(dataPoint))
            return null;

        JsonObject object = new JsonObject(dataPoint.getEdgeJson());
        JsonObject extRule = object.getJsonObject("extRule");
        String reverseFormula = extRule.getString("reverseFormula");
        String unit = extRule.getString("unit");

        dataPoint.setReverseFormula(reverseFormula);
        dataPoint.setUnit(unit);
        return dataPoint;
    }

    @Override
    @Cacheable(value = ConstantCacheKey.CACHE_DATA_POINT_TEMPLATE, key = "#dataPointTemplateId")
    public DataPointTemplate getDataPointTemplate(Integer dataPointTemplateId) {
        return dataPointMapper.getDataPointTemplate(dataPointTemplateId);
    }

    @Cacheable(value = CACHE_DATA_POINT_REL, key = "#dataPointTemplateId")
    public List<TargetDataPointDTO> getDataPointListByDataPointTemplateId(Integer dataPointTemplateId) {
        return dataPointMapper.getDataPointListByDataPointTemplateId(dataPointTemplateId);
    }
}
