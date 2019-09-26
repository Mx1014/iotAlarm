package cn.usr.cloud.alarm.service.impl;

import cn.usr.cloud.alarm.service.EmailTask;
import freemarker.template.Configuration;
import io.reactivex.Observable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


/**
 * @Package: cn.usr.alarm.broadcast.service
 * @Description: TODO
 * @author: Rock 【shizhiyuan@usr.cn】
 * @Date: 2018/3/13 0013 14:49
 */
@Component
@Slf4j
public class EmailTaskImpl implements EmailTask {

    @Autowired
    Configuration configuration;

    @Override
    public void pushEmailMessage(String email,String alarmMsg) {

        Observable.fromCallable(()->sendEmail(email, alarmMsg))
                .subscribe(code->{
                    if(code == -1){
                        log.info("邮件发送失败！");
                    }
                },throwable -> {

                });

    }

    @Value("${email.account:cloud@usr.cn}")
    String account;

    @Value("${email.authcode:YouRenYun153}")
    String authCode;

    @Value("${email.server:smtp.exmail.qq.com}")
    String emailServer;


    /**
     * 发送邮件
     *
     * @param addressee     收件地址
     * @param content       邮件内容
     * @return 是否发送成功
     */
    public int sendEmail(  String addressee, String content) {
        int flag;

        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");// 连接协议
        properties.put("mail.smtp.host", emailServer);// 主机名
        properties.put("mail.smtp.port", "465");// 端口号
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");//设置是否使用ssl安全连接  ---一般都使用
        properties.put("mail.debug", "true");//设置是否显示debug信息  true 会在控制台显示相关信息
        properties.put("mail.smtp.connectiontimeout", 5000);
        properties.put("mail.smtp.timeout", 5000);
        //得到回话对象
        Session session = Session.getInstance(properties);

        // 获取邮件对象
        Message message = new MimeMessage(session);
        try {
            //设置发件人邮箱地址
            message.setFrom(new InternetAddress(account));
            //设置收件人地址
            message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(addressee)});
            //设置邮件标题
            // message.setSubject("密码重置测试邮件");
            message.setSubject("报警信息");
            //设置邮件内容
            // message.setText("这是一个密码重置 测试邮件");
            message.setContent(content, "text/html;charset=utf-8");
            //得到邮件对象
            Transport transport = session.getTransport();
            //连接自己的邮箱账户
            transport.connect(account, authCode);//密码为刚才得到的授权码

            //发送邮件
            transport.sendMessage(message, message.getAllRecipients());
            flag = 0;
        } catch (AddressException e) {
            flag = -1;
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            flag = -1;
        } catch (MessagingException e) {
            e.printStackTrace();
            flag = -1;
        }


        return flag;
    }
}
