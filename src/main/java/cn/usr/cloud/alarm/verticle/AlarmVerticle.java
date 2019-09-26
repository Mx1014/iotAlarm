package cn.usr.cloud.alarm.verticle;

import cn.usr.cloud.alarm.config.MqttConfig;
import cn.usr.cloud.alarm.constant.ConstanControlTargetType;
import cn.usr.cloud.alarm.dto.AlarmContactsDTO;
import cn.usr.cloud.alarm.dto.AlarmInfoDTO;
import cn.usr.cloud.alarm.entity.DataPointInfo;
import cn.usr.cloud.alarm.entity.Linkage;
import cn.usr.cloud.alarm.entity.Trigger;
import cn.usr.cloud.alarm.eventbus.AlarmEventBus;
import cn.usr.cloud.alarm.eventbus.KafkaEventCodec;
import cn.usr.cloud.alarm.eventbus.event.KafkaEvent;
import cn.usr.cloud.alarm.eventbus.event.MqttEvent;
import cn.usr.cloud.alarm.mapper.one.DataPointAlarmMapper;
import cn.usr.cloud.alarm.mapper.one.DataPointMapper;
import cn.usr.cloud.alarm.service.*;
import cn.usr.cloud.alarm.util.EmptyUtil;
import com.google.common.eventbus.Subscribe;
import freemarker.template.TemplateException;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.fusesource.mqtt.client.CallbackConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class AlarmVerticle extends AbstractVerticle {

    @Autowired
    AlarmLogicService alarmLogicService;

    @Autowired
    AlarmNotifyService alarmNotifyService;

    @Autowired
    AlarmConfigService alarmConfigService;

    @Autowired
    AlarmEventBus alarmEventBus;

    @Autowired
    DataPointAlarmMapper dataPointAlarmMapper;

    @Autowired
    DataPointMapper dataPointMapper;

    @Autowired
    LinkageControlService linkageControlService;

    EventBus eb;

    @Autowired
    CallbackConnection mqttConnection;

    @Autowired
    MqttConfig.MqttActionCallback callback;

    @Autowired
    ProtocolTopicService topicService;

    @Autowired
    ProtocolPackageService packageService;

    @Autowired
    SlaveService slaveService;

    @PostConstruct
    public void postConstruct() {
        alarmEventBus.register(this);
        Vertx.vertx(new VertxOptions().setWorkerPoolSize(10)).deployVerticle(this);
    }

    @PreDestroy
    public void preDestroy() {
        alarmEventBus.unregister(this);
    }

    @Subscribe
    public void onAlarmRawData(MqttEvent mqttEvent) {
//        log.info("接受原始数据，event:{}", mqttEvent);
//        if (null != eb) {
//            eb.publish("alarm", mqttEvent.msg);
//        }
    }

    /**
     * 监听test主题,有消息就读取
     * @param message
     */
    @KafkaListener(topics = {"PlcnetToAlarm","ModbusToAlarm"}, containerFactory = "insideKafkaListenerContainerFactory")
    public void consumer(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic, @Payload String message){
        log.info("接受原始数据，message:{}", message);
        String moduleName = topic.split("To")[0];
        KafkaEvent kafkaEvent = new KafkaEvent(null , moduleName, message);
        if (null != eb) {
            eb.publish("alarm", kafkaEvent);
        }
    }

    @Override
    public void start() {
        eb = vertx.eventBus();
        eb.registerDefaultCodec(KafkaEvent.class, new KafkaEventCodec());
        MessageConsumer<KafkaEvent> consumer = eb.consumer("alarm");

        consumer.handler(msg -> {
            KafkaEvent kafkaEvent = msg.body();
            JsonObject object = new JsonObject(kafkaEvent.msg);
            String deviceId = object.getString("deviceId");
            JsonArray dataPoints = object.getJsonArray("dataPoints");
            for(int i=0; i < dataPoints.size(); i++){
                JsonObject dataPointJson = dataPoints.getJsonObject(i);
                Integer dataPointId = dataPointJson.getInteger("dataPointId");
                Double value = dataPointJson.getDouble("value");
                Long timeStamp = dataPointJson.getLong("timeStamp");

                List<Trigger> triggerList = alarmConfigService.getTriggerByDataPointId(dataPointId);
                if(EmptyUtil.isEmpty(triggerList)) return;

                //获取报警相关数据
                AlarmInfoDTO alarmInfoDTO = alarmLogicService.getAlarmInfo(deviceId, dataPointId);
                alarmInfoDTO.setDataPointRelId(dataPointId);
                triggerList.forEach(trigger -> {
                    trigger.setDeviceId(deviceId);
                    trigger.setInputValue(value);
                    alarmInfoDTO.setTrigger(trigger);
                    alarmInfoDTO.setAlarmTime(timeStamp);
                    //当前缓存的数据点的报警状态
                    boolean cacheAlarmState = alarmLogicService.hasTriggered(deviceId, dataPointId, trigger.getId());
                    //当前数值是否触发带死区报警
                    boolean triggerDeadZoneAlarmState = trigger.isSufficientDeadZoneValue(cacheAlarmState);
                    //当前数值是否触发不带死区报警
                    //boolean triggerAlarmState = trigger.isSufficientDeadZoneValue(cacheAlarmState);

                    alarmInfoDTO.setAlarmState(triggerDeadZoneAlarmState);

                    if (cacheAlarmState != triggerDeadZoneAlarmState) {//状态变化了
                        log.info("状态变化了");
                        //设置触发器的状态
                        if(triggerDeadZoneAlarmState){
                            alarmLogicService.setTriggered(deviceId, dataPointId, trigger.getId(), value);
                        }else{
                            alarmLogicService.removeTriggered(deviceId, dataPointId, trigger.getId());
                        }

                        alarmNotifyService.notifyToCenter(alarmInfoDTO);
                        List<AlarmContactsDTO> alarmContactsDtoS = alarmConfigService.getContactListByTriggerId(trigger.getId());
                        alarmContactsDtoS.forEach(alarmContactsDto -> {
                            try {
                                alarmNotifyService.publish(alarmContactsDto, alarmInfoDTO);
                            } catch (IOException | TemplateException e) {
                                e.printStackTrace();
                            }
                        });
                        DataPointInfo dataPointInfo = alarmInfoDTO.getDataPointInfo();
                        //只有触发报警&联动控制启用时才联动
                        if(triggerDeadZoneAlarmState && trigger.getLinkageControlStatus() == 1){

                            Linkage linkage = alarmConfigService.getLinkageByTriggerId(trigger.getId(), deviceId);
                            linkageControlService.control(linkage, kafkaEvent.moduleName);
                            alarmNotifyService.triggerRecord(trigger, linkage, dataPointInfo, triggerDeadZoneAlarmState, timeStamp);
                        }else{
                            alarmNotifyService.triggerRecord(trigger, null, dataPointInfo, triggerDeadZoneAlarmState, timeStamp);
                        }
                    }else if(triggerDeadZoneAlarmState){
                        //todo 状态没有变化，通知又满足了报警
                        log.info("状态没有变化但是满足了报警");
                    }
                });
            }
        });
    }
}
