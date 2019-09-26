package cn.usr.cloud.alarm.listener;

import cn.usr.cloud.alarm.eventbus.MqttSysEventBus;
import cn.usr.cloud.alarm.eventbus.event.MqttEvent;
import lombok.extern.slf4j.Slf4j;
import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Listener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MqttListener implements Listener {
    @Autowired
    MqttSysEventBus mqttSysEventBus;


    public void onDisconnected() {
        mqttSysEventBus.post(0);
    }

    @Override
    public void onFailure(Throwable throwable) {
        mqttSysEventBus.post(0);
    }


    public void onConnected() {
    }

    @Override
    public void onPublish(UTF8Buffer topic, Buffer payload, Runnable ack) {
        // 处理来自topic的消息
        log.info("receive topic:{} and message:{}", topic.toString(), new String(payload.toByteArray()));
        ack.run();
        MqttEvent mqttEvent = new MqttEvent(topic.toString(), "plcnet" ,new String(payload.toByteArray()));
        mqttSysEventBus.post(mqttEvent);
    }



}