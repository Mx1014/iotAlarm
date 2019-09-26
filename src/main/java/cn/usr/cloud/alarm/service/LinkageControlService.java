package cn.usr.cloud.alarm.service;


import cn.usr.cloud.alarm.entity.Linkage;

public interface LinkageControlService {
    void control(Linkage linkage, String moduleName);

    Linkage getLinkageByTriggerId(Integer triggerId);
}
