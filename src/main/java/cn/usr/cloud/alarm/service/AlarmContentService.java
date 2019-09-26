package cn.usr.cloud.alarm.service;


import cn.usr.cloud.alarm.dto.AlarmInfoDTO;
import cn.usr.cloud.alarm.entity.DataPointInfo;
import cn.usr.cloud.alarm.entity.DeviceInfo;
import cn.usr.cloud.alarm.entity.Trigger;
import freemarker.template.TemplateException;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;

import java.io.IOException;
import java.util.List;

public interface AlarmContentService {
    String getSmsContent(AlarmInfoDTO alarmInfoDTO) throws IOException, TemplateException;
    String getVmsContent(AlarmInfoDTO alarmInfoDTO);
    String getEmailContent(AlarmInfoDTO alarmInfoDTO);
    List<WxMpTemplateData> getWeChatContent(AlarmInfoDTO alarmInfoDTO);

    String getPlainContent(Trigger trigger, boolean alarmState);
}
