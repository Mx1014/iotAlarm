package cn.usr.cloud.alarm.listener;

import cn.usr.cloud.alarm.constant.ConstanTriggerType;
import cn.usr.cloud.alarm.entity.DeviceInfo;
import cn.usr.cloud.alarm.entity.Trigger;
import cn.usr.cloud.alarm.mapper.one.DataPointMapper;
import cn.usr.cloud.alarm.mapper.one.TriggerAlarmMapper;
import cn.usr.cloud.alarm.service.AlarmLogicService;
import cn.usr.cloud.alarm.service.CleanCacheService;
import cn.usr.cloud.alarm.service.DeviceService;
import cn.usr.cloud.alarm.util.EmptyUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * 用于接收缓存更新通知
 */
@Slf4j
@Component
public class KafkaReceiveListener {

    @Autowired
    private CleanCacheService cleanCacheService;

    @Autowired
    private DataPointMapper dataPointMapper;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private AlarmLogicService alarmLogicService;

    @Autowired
    private TriggerAlarmMapper triggerAlarmMapper;


    @KafkaListener(topics = "${local.sync-device-topic}", containerFactory = "externalKafkaListenerContainerFactory")
    public void syncDevice(String message) {
        log.info("syncDevice:{}", message);

        JSONObject jsonObject = JSONObject.parseObject(message);
        String action = jsonObject.getString("action");
        //下边这两种action用不到
        if("enable".equals(action) || "disable".equals(action)) return;

        JSONArray deviceNos = jsonObject.getJSONArray("deviceNos");
        deviceNos.forEach(deviceNoStr -> {
            String deviceNo = deviceNoStr.toString();
            DeviceInfo deviceInfo = deviceService.getDeviceByDeviceId(deviceNo);

            cleanCacheService.cleanDevice(deviceNo);

            cleanDeviceAlarmState(deviceNo);

            List<Integer> dataPointRelIds = dataPointMapper.getDataPointRelIdsByDeviceNo(deviceNo);
            dataPointRelIds.forEach(relId -> {
                //删除设备相关的数据点缓存
                cleanCacheService.cleanDataPoint(relId);
                //删除相关触发器
                cleanCacheService.cleanTrigger(relId);
            });
            //删除关联表缓存
            if(EmptyUtil.isNotEmpty(deviceInfo) && EmptyUtil.isNotEmpty(deviceInfo.getTemplateId())){
                List<Integer> dataPointTemplateIds = dataPointMapper.getDataPointTemplateIdsByDeviceTemplateId(deviceInfo.getTemplateId());
                dataPointTemplateIds.forEach(dataPointTemplateId -> cleanCacheService.cleanDataPointRel(dataPointTemplateId));
            }
        });
    }

    @KafkaListener(topics = {"syncTrigger"}, containerFactory = "externalKafkaListenerContainerFactory")
    public void syncTrigger(String message) {
        log.info("syncTrigger:{}", message);

        JSONObject jsonObject = JSONObject.parseObject(message);
        JSONArray triggers = jsonObject.getJSONArray("triggers");
        String action = jsonObject.getString("action");
        triggers.forEach(triggerJson -> {
            JSONObject trigger = (JSONObject) triggerJson;
            Integer triggerId = trigger.getInteger("triggerId");

            cleanCacheService.cleanContacts(triggerId);
            //删除相关触发器的报警状态
            if(!action.contains("Contacts")){
                JSONArray dataPointIds = trigger.getJSONArray("dataPointIds");
                //触发器相关的操作
                //删除触发器缓存
                cleanTriggerByTriggerIdDataPointId(triggerId, dataPointIds);

                //删除报警联动
                cleanCacheService.cleanLinkage(triggerId);
                //删除该触发器的报警状态
                cleanTriggerAlarmState(triggerId);
            }

        });
    }

    @KafkaListener(topics = {"syncTemplate"}, containerFactory = "externalKafkaListenerContainerFactory")
    public void syncTemplate(String message) {
        log.info("syncTemplate:{}", message);

        JSONObject jsonObject = JSONObject.parseObject(message);
        JSONArray templateIds = jsonObject.getJSONArray("templateIds");
        if(EmptyUtil.isEmpty(templateIds)) return;
        templateIds.forEach(templateId -> {
            Integer deviceTemplateId = Integer.valueOf(templateId.toString());
            //根据设备模板获取下边的所有数据点模板
            List<Integer> dataPointIds = dataPointMapper.getDataPointRelIdsByDeviceTemplateId(deviceTemplateId);
            //删除相关联的从机
            cleanCacheService.cleanSlave(deviceTemplateId);

            if(EmptyUtil.isNotEmpty(dataPointIds)) {
                //根据真实id删除触发器列表
                dataPointIds.forEach(dataPointId -> {
                    //根据真实id删除触发器列表
                    cleanCacheService.cleanTrigger(dataPointId);
                    //删除数据点缓存
                    cleanCacheService.cleanDataPoint(dataPointId);
                    //删除数据点的报警状态
                    cleanDataPointAlarmState(dataPointId);
                });
            }
            //删除关联表缓存
            List<Integer> dataPointTemplateIds = dataPointMapper.getDataPointTemplateIdsByDeviceTemplateId(deviceTemplateId);
            dataPointTemplateIds.forEach(dataPointTemplateId -> cleanCacheService.cleanDataPointRel(dataPointTemplateId));
        });

    }

    @KafkaListener(topics = {"syncUser"}, containerFactory = "externalKafkaListenerContainerFactory")
    public void syncUser(String message) {
        log.info("syncUser:{}", message);

        JSONObject jsonObject = JSONObject.parseObject(message);
        JSONArray userIds = jsonObject.getJSONArray("userIds");
        userIds.forEach(userId -> cleanCacheService.cleanUser(userId.toString()));
    }


    /**
     * 根据triggerId清除该触发器的报警状态
     * @param triggerId
     */
    private void cleanTriggerAlarmState(Integer triggerId) {
        try{
            String key = alarmLogicService.getRedisKeyForAlarmState(null, null, triggerId);
            Set<String> keys = redisTemplate.keys(key);
            if(EmptyUtil.isEmpty(key)) return;
            redisTemplate.delete(keys);
            alarmLogicService.updateAlarmStatusForIgnite(keys);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 根据deviceNo清除该设备的报警状态
     * @param deviceNo
     */
    private void cleanDeviceAlarmState(String deviceNo) {
        try{
            String key = alarmLogicService.getRedisKeyForAlarmState(deviceNo, null, null);
            Set<String> keys = redisTemplate.keys(key);
            redisTemplate.delete(keys);
            alarmLogicService.updateAlarmStatusForIgnite(keys);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 根据dataPointId清除该数据点的报警状态
     * @param dataPointId
     */
    private void cleanDataPointAlarmState(Integer dataPointId) {
        try{
            String key = alarmLogicService.getRedisKeyForAlarmState(null, dataPointId, null);
            Set<String> keys = redisTemplate.keys(key);
            redisTemplate.delete(keys);
            alarmLogicService.updateAlarmStatusForIgnite(keys);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    private void cleanTriggerByTemlateId(Integer templateId) {
        //根据设备模板获取下边的所有数据点模板
        List<Integer> dataPointIds = dataPointMapper.getDataPointRelIdsByDeviceTemplateId(templateId);
        if(EmptyUtil.isEmpty(dataPointIds)) return;

        //根据真实id删除触发器列表
        dataPointIds.forEach(dataPointId -> cleanCacheService.cleanTrigger(dataPointId));

    }

    private void cleanTriggerByTriggerIdDataPointId(Integer triggerId, JSONArray dataPointIds) {
        try{
            Trigger trigger = triggerAlarmMapper.getTriggerById(triggerId);
            if(EmptyUtil.isEmpty(trigger)) return;
            //删除触发器列表
            if(ConstanTriggerType.TRIGGER_TYPE_TEMPLATE.equals(trigger.getType())){
                //模板触发器
                dataPointIds.forEach(dataPointTemplateId -> {
                    List<Integer> pointIds = dataPointMapper.getDataPointRelIdsByDataPointTemplateId((Integer) dataPointTemplateId);
                    pointIds.forEach(dataPointId -> cleanCacheService.cleanTrigger(dataPointId));
                });

            }else if(ConstanTriggerType.TRIGGER_TYPE_DEVICE.equals(trigger.getType())){
                //设备(独立)触发器
                dataPointIds.forEach(dataPointId ->
                    cleanCacheService.cleanTrigger((Integer) dataPointId)
                );
            }

        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

}