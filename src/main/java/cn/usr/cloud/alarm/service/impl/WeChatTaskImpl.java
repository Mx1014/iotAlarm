package cn.usr.cloud.alarm.service.impl;

import cn.usr.cloud.alarm.service.WeChatTask;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.stereotype.Component;

import java.util.List;

import static cn.usr.cloud.alarm.constant.ConstantProperty.USR_APPID;
import static cn.usr.cloud.alarm.constant.ConstantProperty.USR_APPSECRET;
import static cn.usr.cloud.alarm.constant.ConstantProperty.USR_TEMPLATEID;

/**
 * @Package: cn.usr.alarm.broadcast.service
 * @Description: TODO
 * @author: Rock 【shizhiyuan@usr.cn】
 * @Date: 2018/3/13 0013 08:58
 */
@Component
@Slf4j
public class WeChatTaskImpl implements WeChatTask {

    /**
     * 微信推送
     * <p>
     * // 1.根据用户等级去查询报警信息
     * // 2.获取组织模板消息
     * // 3.判断是否中性 然后推送消息获取结果
     * jar文档：https://binarywang.github.io/weixin-java-mp-javadoc/
     * @return
     * @throws Exception
     */
    @Override
    public void pushWeChatTemplateMessage(String openId, List<WxMpTemplateData> data) throws WxErrorException {

        //配置
        WxMpInMemoryConfigStorage wxMpInMemoryConfigStorage = new WxMpInMemoryConfigStorage();
        wxMpInMemoryConfigStorage.setAppId(USR_APPID);
        wxMpInMemoryConfigStorage.setSecret(USR_APPSECRET);
        //授权
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpInMemoryConfigStorage);
        //组成模板消息
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder().templateId(USR_TEMPLATEID).toUser(openId).build();
        templateMessage.getData().addAll(data);
        wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);

    }

}
