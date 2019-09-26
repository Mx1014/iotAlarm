package cn.usr.cloud.alarm.service.impl;

import cn.usr.cloud.alarm.config.MqttConfig;
import cn.usr.cloud.alarm.constant.ConstanControlTargetType;
import cn.usr.cloud.alarm.entity.DataPointInfo;
import cn.usr.cloud.alarm.entity.Linkage;
import cn.usr.cloud.alarm.mapper.one.AlarmMapper;
import cn.usr.cloud.alarm.mapper.one.DataPointAlarmMapper;
import cn.usr.cloud.alarm.service.LinkageControlService;
import cn.usr.cloud.alarm.service.ProtocolPackageService;
import cn.usr.cloud.alarm.service.ProtocolTopicService;
import cn.usr.cloud.alarm.util.EmptyUtil;
import cn.usr.cloud.alarm.util.ScriptHandler;
import lombok.extern.slf4j.Slf4j;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.QoS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static cn.usr.cloud.alarm.constant.ConstantCacheKey.CACHE_LINKAGE;

/**
 * @Auther: Andy(李永强)
 * @Date: 2019/5/31 下午2:57
 * @Describe:
 */
@Service
@Slf4j
public class LinkageControlServiceImpl implements LinkageControlService {

    @Autowired
    DataPointAlarmMapper dataPointAlarmMapper;

    @Autowired
    AlarmMapper alarmMapper;

    @Autowired
    ProtocolTopicService topicService;

    @Autowired
    ProtocolPackageService packageService;

    @Autowired
    CallbackConnection mqttConnection;

    @Autowired
    MqttConfig.MqttActionCallback callback;

    @Resource(name = "insideKafkaTemplate")
    private KafkaTemplate<String, String> insideKafkaTemplate;

    @Resource(name = "externalKafkaTemplate")
    private KafkaTemplate<String, String> externalKafkaTemplate;

    @Override
    public void control(Linkage linkage, String moduleName) {

        String payload = packageService.linkageDevicePackage(linkage);
        String topic = topicService.topicSendToModule(linkage.getControlTargetType(), moduleName);
        log.info("【联动控制】：topic:{},payload:{}", topic, payload);
        //linkage.getControlTargetType()
        if(ConstanControlTargetType.CONTROL_SELF.equals(linkage.getControlTargetType())){
            insideKafkaTemplate.send(topic, payload);
        }else{
            //联动其他设备，发送到外部Kafka
            externalKafkaTemplate.send(topic, payload);
        }

    }

    @Cacheable(value = CACHE_LINKAGE, key = "#triggerId")
    public Linkage getLinkageByTriggerId(Integer triggerId) {
        return alarmMapper.getLinkageByTriggerId(triggerId);
    }
}
