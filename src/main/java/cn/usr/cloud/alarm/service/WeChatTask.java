package cn.usr.cloud.alarm.service;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;

import java.util.List;

/**
 * @Package: cn.usr.alarm.broadcast.service
 * @Description: TODO
 * @author: Rock 【shizhiyuan@usr.cn】
 * @Date: 2018/3/14 0014 15:32
 */
public interface WeChatTask {

    /**
     * 推送微信模板消息
     *
     * @param alarmContactsDto
     * @return
     */
    void pushWeChatTemplateMessage(String weChatOpenId, List<WxMpTemplateData> template) throws WxErrorException;
}
