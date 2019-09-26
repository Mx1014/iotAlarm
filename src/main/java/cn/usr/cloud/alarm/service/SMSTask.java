package cn.usr.cloud.alarm.service;

/**
 * @author 赵震
 * @date 2018-06-21 14:38
 */
public interface SMSTask {

    /**
     * 使用阿里云发送报警通知
     * @param telephone
     * @param jsonParam json格式，短信模板的输入参数
     */
    void pushSMSMessage(String telephone, String jsonParam);

}
