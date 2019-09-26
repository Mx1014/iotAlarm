package cn.usr.cloud.alarm.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @Auther: Andy(李永强)
 * @Date: 2019/4/28 上午10:27
 * @Describe:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitMqTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testRabbitMqRecevie(){
//        AliyunIotAccountEntity aliyunIotAccountEntity = new AliyunIotAccountEntity();
//        aliyunIotAccountEntity.setAccessKey("1111");
//        aliyunIotAccountEntity.setId(1);
//        aliyunIotAccountEntity.setSystemId(2);
//        rabbitTemplate.convertAndSend(EXCHANGE_CENTER_ALARM,ROUTING_KEY_DECODE, ProtoStuffSerializerUtil.serialize(aliyunIotAccountEntity));
    }
}
