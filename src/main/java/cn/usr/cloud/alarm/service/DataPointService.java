package cn.usr.cloud.alarm.service;


import cn.usr.cloud.alarm.dto.TargetDataPointDTO;
import cn.usr.cloud.alarm.entity.DataPointInfo;
import cn.usr.cloud.alarm.entity.DataPointTemplate;

import java.util.List;

/**
 * Created by pi on 2018/10/10.
 */
public interface DataPointService {
    DataPointInfo getDataPointById(Integer dataPointId);

    DataPointTemplate getDataPointTemplate(Integer dataPointTemplateId);

    List<TargetDataPointDTO> getDataPointListByDataPointTemplateId(Integer dataPointTemplateId);
}
