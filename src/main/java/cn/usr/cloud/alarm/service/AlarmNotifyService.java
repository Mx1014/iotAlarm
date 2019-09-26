package cn.usr.cloud.alarm.service;

import cn.usr.cloud.alarm.dto.AlarmContactsDTO;
import cn.usr.cloud.alarm.dto.AlarmInfoDTO;
import cn.usr.cloud.alarm.entity.DataPointInfo;
import cn.usr.cloud.alarm.entity.Linkage;
import cn.usr.cloud.alarm.entity.Trigger;
import freemarker.template.TemplateException;

import java.io.IOException;

public interface AlarmNotifyService {
    void publish(AlarmContactsDTO contactsDto, AlarmInfoDTO alarmInfoDTO) throws IOException, TemplateException;
    void notifyToCenter(AlarmInfoDTO alarmInfoDTO);
    void triggerInfoPublish(AlarmContactsDTO contactsDto, AlarmInfoDTO alarmInfoDTO) throws IOException, TemplateException;
    void recoverInfoPublish(AlarmContactsDTO contactsDto, AlarmInfoDTO alarmInfoDTO) throws IOException, TemplateException;
    void triggerRecord(Trigger trigger, Linkage linkage, DataPointInfo dataPointInfo, boolean alarmState, Long timeStamp);
}
