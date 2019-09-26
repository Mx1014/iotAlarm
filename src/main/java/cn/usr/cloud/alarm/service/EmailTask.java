package cn.usr.cloud.alarm.service;

/**
 * @Package: cn.usr.alarm.broadcast.service
 * @Description: TODO
 * @author: Rock 【shizhiyuan@usr.cn】
 * @Date: 2018/3/14 0014 15:38
 */
public interface EmailTask {


    /**
     * 推送邮件模板消息
     *
     * @return
     */
    void pushEmailMessage(String email, String alarmMsg);
}
