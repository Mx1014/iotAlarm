package cn.usr.cloud.alarm.service.impl;

import cn.usr.cloud.alarm.service.SMSTask;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import io.reactivex.Observable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import static cn.usr.cloud.alarm.constant.ConstantProperty.*;


@Component
@Slf4j
public class SMSTaskImpl implements SMSTask {

    @Override
    public void pushSMSMessage(String telephone,String jsonParam) {

        Observable.fromCallable(()->{
            SendSmsResponse smsResult = this.sendAlarmSms(telephone,  jsonParam);
            return smsResult;
        }).subscribe(sendSmsResponse -> {
            if (sendSmsResponse.getCode() != null && ALIYUN_SMS_STATE_SUCCESS.equals(sendSmsResponse.getCode())){
                log.info("正常发送");
            }else{
               log.warn("发送失败");
            }
        },throwable -> {
            log.error("【异常】发送短信消息异常,{}", throwable);
        });
    }

     /**
     * 发送短信报警功能
     *
     * @param phoneNumber       手机号码
     * @param smsTemplateParam  模板替换参数列表（Json格式的字符串）
     * @return
     * @throws ClientException
     */
    public SendSmsResponse sendAlarmSms(String phoneNumber,String smsTemplateParam) throws ClientException {
        String smsSignName = ALIYUN_SMS_SIGNNAME;
        String smsTemplateCode = ALIYUN_SMS_TEMPLATE;
        String accKeyId = ALIYUN_ACCESSKEY_ID;
        String accKeySecret = ALIYUN_ACCESSKEY_SECRET;

        //设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //设置accessKeyId和accessKeySecret
        final String accessKeyId = accKeyId;
        final String accessKeySecret = accKeySecret;

        //初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile(ALIYUN_SMS_REGION_HANGZHOU, accessKeyId,
                accessKeySecret);
        DefaultProfile.addEndpoint(ALIYUN_SMS_REGION_HANGZHOU, ALIYUN_SMS_REGION_HANGZHOU, ALIYUN_SMS_PRODUCT,
                ALIYUN_SMS_DOMAIN);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        //使用post提交
        request.setMethod(MethodType.POST);

        //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为00+国际区号+号码，如“0085200000000”
        request.setPhoneNumbers(phoneNumber);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(smsSignName);
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(smsTemplateCode);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        request.setTemplateParam(smsTemplateParam);

        //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        //request.setOutId("yourOutId");

        //请求失败这里会抛ClientException异常
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

        return sendSmsResponse;

    }
}
