package cn.usr.cloud.alarm.service;


import cn.usr.cloud.alarm.entity.SlaveInfo;

import java.util.List;

/**
 * Created by pi on 2018/10/10.
 */
public interface SlaveService {

    List<SlaveInfo> getSlaveListBytemplateId(Integer deviceTemplateId);
}
