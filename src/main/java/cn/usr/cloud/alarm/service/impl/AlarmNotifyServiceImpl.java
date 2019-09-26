package cn.usr.cloud.alarm.service.impl;

import cn.usr.cloud.alarm.dto.AlarmContactsDTO;
import cn.usr.cloud.alarm.dto.AlarmInfoDTO;
import cn.usr.cloud.alarm.entity.*;
import cn.usr.cloud.alarm.eventbus.MqttSysEventBus;
import cn.usr.cloud.alarm.mapper.one.AlarmMapper;
import cn.usr.cloud.alarm.service.*;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import freemarker.template.TemplateException;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.QoS;
import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static cn.usr.cloud.alarm.constant.ConstantCacheKey.CACHE_ALARM_RECORD;
import static cn.usr.cloud.alarm.constant.ConstantCacheKey.CACHE_ALARM_RECORD_MYSQL;

@Slf4j
@Service
public class AlarmNotifyServiceImpl implements AlarmNotifyService {
    @Autowired
    MqttSysEventBus mqttSysEventBus;

    @Autowired
    AlarmContentService alarmContentService;

    @Autowired
    WeChatTask weChatTask;

    @Autowired
    EmailTask emailTask;

    @Autowired
    SMSTask smsTask;

    @Autowired
    VMSTask vmsTask;

    @Autowired
    AlarmMapper alarmDao;

    @Autowired
    CallbackConnection mqttConnection;

    @Autowired
    ProtocolTopicService protocolTopicService;

    @Autowired
    ProtocolPackageService protocolPackageService;

    @Autowired
    RedisTemplate redisTemplate;

    private static ExecutorService es = Executors.newFixedThreadPool(20, new ThreadFactoryBuilder().setNameFormat("notifyThread-%d").build());

    @Override
    public void publish(AlarmContactsDTO contactsDto, AlarmInfoDTO alarmInfoDTO) throws IOException, TemplateException {

        Trigger trigger = alarmInfoDTO.getTrigger();

        Observable<AlarmContactsDTO> observable = Observable.defer(() -> Observable.just(contactsDto).observeOn(Schedulers.from(es)));

        //短信报警
        if (trigger.getAlarmMode().indexOf("0") > -1) {
            String alarmMsg = alarmContentService.getSmsContent(alarmInfoDTO);
            observable.map(contact -> contact.getTel())
                    .subscribe(telephone -> smsTask.pushSMSMessage(telephone, alarmMsg),
                            throwable -> log.warn("短信发送失败", throwable)
                    );
        }

        //微信报警
        if (trigger.getAlarmMode().indexOf("1") > -1) {
            List<WxMpTemplateData> wechatMsg = alarmContentService.getWeChatContent(alarmInfoDTO);
            observable.filter(alarmContactsDto -> alarmContactsDto.getOpenId() != null)
                    .map(alarmContactsDto -> alarmContactsDto.getOpenId())
                    .subscribe(openId -> weChatTask.pushWeChatTemplateMessage(openId, wechatMsg));
        }

        //邮件报警
        if (trigger.getAlarmMode().indexOf("2") > -1) {
            String alarmMsg = alarmContentService.getEmailContent(alarmInfoDTO);
            log.info("开始，发送邮件");
            observable.map(contact -> contact.getEmail())
                    .filter(email -> email != null)
                    .subscribe(email -> emailTask.pushEmailMessage(email, alarmMsg));
            log.info("完成，发送邮件");
        }

        //语音报警
        if (trigger.getAlarmMode().indexOf("3") > -1) {
            String alarmMsg = alarmContentService.getVmsContent(alarmInfoDTO);
            log.info("开始，发送语音");
            observable.map(contact -> contact.getTel())
                    .filter(tel -> tel != null)
                    .subscribe(tel -> vmsTask.pushVMSMessage(tel, alarmMsg));
            log.info("完成，发送邮件");
        }
    }

    @Override
    public void notifyToCenter(AlarmInfoDTO alarmInfoDTO) {
        log.info("往前端推送");
        UserInfo userInfo = alarmInfoDTO.getUserInfo();
        //往前端推送
        String paylaod = protocolPackageService.frontenfAlarmPackage(alarmInfoDTO);
        String topic = protocolTopicService.frontendTopicForDeviceStatus(userInfo);
        log.info("topic:{}",topic);
        mqttConnection.publish(topic,
                paylaod.getBytes(),
                QoS.AT_MOST_ONCE, false,
                new Callback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        log.info("往前端推送成功");
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        log.info("往前端推送失败");
                        log.error(throwable.getMessage(),throwable);
                        mqttSysEventBus.post(0);
                    }
                });
    }

    @Override
    public void triggerInfoPublish(AlarmContactsDTO contactsDto, AlarmInfoDTO alarmInfoDTO) throws IOException, TemplateException {
        Trigger trigger = alarmInfoDTO.getTrigger();
        UserInfo userInfo = alarmInfoDTO.getUserInfo();

        String mqttPaylaod = protocolPackageService.frontenfAlarmPackage(alarmInfoDTO);
        mqttConnection.publish(protocolTopicService.frontendTopicForDeviceStatus(userInfo),
                mqttPaylaod.getBytes(),
                QoS.AT_MOST_ONCE, false,
                new Callback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        mqttSysEventBus.post(0);
                    }
                });


        Observable<AlarmContactsDTO> observable = Observable.defer(() -> Observable.just(contactsDto));

        //短信报警
        if (trigger.getAlarmMode().indexOf("0") > -1) {
            String alarmMsg = alarmContentService.getSmsContent(alarmInfoDTO);
            observable.map(contact -> contact.getTel())
                    .subscribe(telephone -> smsTask.pushSMSMessage(telephone, alarmMsg),
                            throwable -> log.warn("短信发送失败", throwable)
                    );
        }

        //微信报警
        if (trigger.getAlarmMode().indexOf("1") > -1) {
            List<WxMpTemplateData> wechatMsg = alarmContentService.getWeChatContent(alarmInfoDTO);
            observable.filter(alarmContactsDto -> alarmContactsDto.getOpenId() != null)
                    .map(alarmContactsDto -> alarmContactsDto.getOpenId())
                    .subscribe(openId -> weChatTask.pushWeChatTemplateMessage(openId, wechatMsg));
        }

        //邮件报警
        if (trigger.getAlarmMode().indexOf("2") > -1) {
            String alarmMsg = alarmContentService.getEmailContent(alarmInfoDTO);
            observable.map(contact -> contact.getEmail())
                    .filter(email -> email != null)
                    .subscribe(email -> emailTask.pushEmailMessage(email, alarmMsg));
        }

        //语音报警
        if (trigger.getAlarmMode().indexOf("3") > -1) {
            String alarmMsg = alarmContentService.getVmsContent(alarmInfoDTO);
            log.info("开始，发送语音");
            observable.map(contact -> contact.getTel())
                    .filter(tel -> tel != null)
                    .subscribe(tel -> vmsTask.pushVMSMessage(tel, alarmMsg));
            log.info("完成，发送邮件");
        }
    }

    @Override
    public void recoverInfoPublish(AlarmContactsDTO contactsDto, AlarmInfoDTO alarmInfoDTO) throws IOException, TemplateException {
        this.triggerInfoPublish(contactsDto, alarmInfoDTO);

    }

// TODO   sendAlarmMessageToCenter()

    @Autowired
    AlarmLogicService alarmLogicService;

    public void triggerRecord(Trigger trigger, Linkage linkage, DataPointInfo dataPointInfo, boolean alarmState, Long timeStamp) {
        /*if (trigger.getInsertType().equals(1)) {
            return;
        }
        if (trigger.getInsertType().equals(0)
                && lastValue.equals(trigger.getInputValue())) {
            return;
        }*/
        //influxDB
        Point.Builder builder = Point.measurement("alarm_record");
        builder.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .tag("TriggerId", trigger.getId().toString())
                .tag("ItemId", dataPointInfo.getItemId())
                .tag("SlaveIndex", trigger.getSlaveIndex())
                .tag("DeviceNo", trigger.getDeviceId());

        String content = alarmContentService.getPlainContent(trigger, alarmState);
        builder.addField("Content", content)
                .addField("UserId", trigger.getUserId())
                .addField("AlarmState", alarmState ? 1 : 0)
                .addField("Value", trigger.getInputValue());

        //mysql
        AlarmHistory alarmHistory = new AlarmHistory();
        alarmHistory.setTriggerId(trigger.getId());
        alarmHistory.setDataPointTemplateId(dataPointInfo.getId());
        alarmHistory.setSlaveIndex(trigger.getSlaveIndex());
        alarmHistory.setDeviceNo(trigger.getDeviceId());

        alarmHistory.setContent(content);
        alarmHistory.setUserId(trigger.getUserId());
        alarmHistory.setAlarmState(alarmState ? 1 : 0);
        alarmHistory.setValue(trigger.getInputValue());
        alarmHistory.setGenerateTime(timeStamp);

        if (linkage != null) {
            //influxDb
            builder.addField("LinkageControlStatus", trigger.getLinkageControlStatus())
                    .addField("ControlType", linkage.getType())
                    .addField("ConstanControlTargetType", linkage.getControlTargetType())
                    .addField("ControlDeviceId", linkage.getDeviceId())
                    .addField("ControlSlaveIndex", linkage.getSlaveIndex())
                    .addField("ControlTemplateId", linkage.getTemplateId())
                    .addField("ControlDataPointId", linkage.getDataPointTemplateId())
                    .addField("ControlSendData", linkage.getSendData());

            //mysql
            alarmHistory.setLinkageControlStatus(linkage.getType());
            alarmHistory.setControlType(linkage.getType());
            alarmHistory.setControlTargetType(linkage.getControlTargetType());
            alarmHistory.setControlDeviceId(linkage.getDeviceId());
            alarmHistory.setControlSlaveIndex(linkage.getSlaveIndex());
            alarmHistory.setControlTemplateId(linkage.getTemplateId());
            alarmHistory.setControlDataPointId(linkage.getDataPointTemplateId());
            alarmHistory.setControlSendData(linkage.getSendData());
        }

        redisTemplate.opsForList().rightPush(CACHE_ALARM_RECORD, builder.build());
        redisTemplate.opsForList().rightPush(CACHE_ALARM_RECORD_MYSQL, alarmHistory);
        //influxDB.write(builder.build());
    }
}
