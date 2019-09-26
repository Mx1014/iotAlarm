package cn.usr.cloud.alarm.service.impl;

import cn.usr.cloud.alarm.service.VMSTask;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dyvmsapi.model.v20170525.SingleCallByTtsRequest;
import com.aliyuncs.dyvmsapi.model.v20170525.SingleCallByTtsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import io.reactivex.Observable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static cn.usr.cloud.alarm.constant.ConstantProperty.*;


@Component
@Slf4j
public class VMSTaskImpl implements VMSTask {

    //产品名称:云通信语音API产品,开发者无需替换
    private static final String Product = "Dyvmsapi";
    //产品域名,开发者无需替换
    private static final String Domain = "dyvmsapi.aliyuncs.com";

    @Override
    public void pushVMSMessage(String telephone,String jsonParam) {

        Observable.fromCallable(()->{
            SingleCallByTtsResponse vmsResult = this.sendAlarmVms(telephone,  jsonParam);
            return vmsResult;
        }).subscribe(vmsResult -> {
            if (vmsResult.getCode() != null && ALIYUN_VMS_STATE_SUCCESS.equals(vmsResult.getCode())){
                log.info("短信发送成功");
            }else{
               log.warn("短信发送失败");
            }
        },throwable -> {
            log.error("【异常】发送短信消息异常,{}", throwable);
        });
    }


    /**
     * 发送短信报警功能
     *
     * @param phoneNumber       手机号码
     * @param vmsTemplateParam  模板替换参数列表（Json格式的字符串）
     * @return
     * @throws ClientException
     */
    public SingleCallByTtsResponse sendAlarmVms(String phoneNumber, String vmsTemplateParam) throws ClientException {
        String showNumber = ALIYUN_VMS_SHOW_NUMBER;
        String vmsTemplateCode = ALIYUN_VMS_TEMPLATE;
        String accKeyId = ALIYUN_ACCESSKEY_ID;
        String accKeySecret = ALIYUN_ACCESSKEY_SECRET;

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accKeyId, accKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", Product, Domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SingleCallByTtsRequest request = new SingleCallByTtsRequest();
        //必填-被叫显号,可在语音控制台中找到所购买的显号
        request.setCalledShowNumber(showNumber);
        //必填-被叫号码
        request.setCalledNumber(phoneNumber);
        //必填-Tts模板ID
        request.setTtsCode(vmsTemplateCode);
        //可选-当模板中存在变量时需要设置此值
        request.setTtsParam(vmsTemplateParam);
        //可选-外部扩展字段,此ID将在回执消息中带回给调用方
        //     request.setOutId("yourOutId");

        //hint 此处可能会抛出异常，注意catch
        SingleCallByTtsResponse singleCallByTtsResponse = acsClient.getAcsResponse(request);

        return singleCallByTtsResponse;

    }
}
