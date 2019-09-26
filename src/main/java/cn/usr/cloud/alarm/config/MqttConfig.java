package cn.usr.cloud.alarm.config;

import cn.usr.cloud.alarm.eventbus.AlarmEventBus;
import cn.usr.cloud.alarm.eventbus.event.MqttEvent;
import cn.usr.cloud.alarm.listener.MqttListener;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.net.URISyntaxException;

@Slf4j
@Configuration
public class MqttConfig {

    @Autowired
    AlarmEventBus alarmEventBus;

    @Autowired
    MqttListener mqttListener;

    @Value("${mqttLink}")
    String mqttLink;

    CallbackConnection callbackConnection;

    @PostConstruct
    public void postConstruct() {
        alarmEventBus.register(this);
    }

    @Subscribe
    public void terminator(Integer code) {
        log.error("mqtt 断开，服务终止");
        System.exit(0);
    }


    /**
     * 文档地址：https://github.com/fusesource/mqtt-client
     * @return
     * @throws URISyntaxException
     */
    @Bean
    public CallbackConnection mqtt() throws URISyntaxException {
        MQTT mqtt = new MQTT();

        mqtt.setHost(mqttLink);
        callbackConnection = mqtt.callbackConnection();

        callbackConnection.listener(new Listener(){

            @Override
            public void onConnected() {
                log.info("mqtt onConnected");
            }

            @Override
            public void onDisconnected() {
                log.info("mqtt onDisconnected");
            }

            @Override
            public void onPublish(UTF8Buffer topic, Buffer payload, Runnable ack) {
                /*log.info("receive topic:{} and message:{}", topic.toString(), new String(payload.toByteArray()));
                ack.run();

                MqttEvent mqttEvent = new MqttEvent(topic.toString(),"plcnet" ,new String(payload.toByteArray()));
                alarmEventBus.post(mqttEvent);*/

            }

            @Override
            public void onFailure(Throwable throwable) {
                log.info("mqtt onFailure");
            }
        });


        callbackConnection.connect(new MqttActionCallback());

        return callbackConnection;
    }

    public class MqttActionCallback implements Callback<Void> {

        @Override
        public void onSuccess(Void aVoid) {
            Topic[] topics = {
                    new Topic("/ALARM/plcnet/u", QoS.AT_MOST_ONCE),
                    new Topic("/ALARM/modbus/u", QoS.AT_MOST_ONCE)
                    //new Topic("/DEVICE/sync", QoS.AT_MOST_ONCE)
            };

            callbackConnection.subscribe(topics, new Callback<byte[]>() {

                @Override
                public void onSuccess(byte[] aByte) {
                    log.debug("hello", aByte.toString());
                }

                @Override
                public void onFailure(Throwable throwable) {

                }
            });
        }

        @Override
        public void onFailure(Throwable throwable) {
            log.error("mqtt客户端异常", throwable);
            alarmEventBus.post(0);
        }
    }

    @Bean
    public MqttActionCallback callback() {
        return new MqttActionCallback();
    }

}
