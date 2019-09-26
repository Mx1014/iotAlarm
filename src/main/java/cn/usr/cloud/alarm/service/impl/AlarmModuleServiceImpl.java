package cn.usr.cloud.alarm.service.impl;

import cn.usr.cloud.alarm.entity.*;
import cn.usr.cloud.alarm.mapper.one.*;
import cn.usr.cloud.alarm.service.AlarmModuleService;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 赵震
 * @date 2018-09-12 14:43
 */
@Slf4j
@Service
public class AlarmModuleServiceImpl implements AlarmModuleService {

    @Autowired
    SlaveMapper deviceSlaveAlarmDao;

    @Autowired
    DeviceAlarmMapper deviceAlarmMapper;

    @Autowired
    DataPointAlarmMapper dataPointAlarmMapper;

    @Autowired
    TriggerAlarmMapper triggerAlarmMapper;

    @Autowired
    UserAlarmMapper userAlarmMapper;


    /**
     * 获取设备信息 根据设备ID
     *
     * @param deviceId
     * @return
     */
    @Override
    public DeviceInfo getDeviceInfoByDeviceId(String deviceId) {
        return deviceAlarmMapper.getDeviceInfoByDeviceId(deviceId);
    }


    /**
     * 获取数据点信息  根据数据点ID
     *
     * @param dataPointId
     * @return
     */
    @Override
    public DataPointInfo getDataPointInfoByDataPointId(long dataPointId) {
        DataPointInfo dataPoint = dataPointAlarmMapper.getDataPointInfoByDataPointId(dataPointId);
        if(dataPoint == null) return null;
        if(dataPoint.getEdgeJson().contains("reverseFormula")){
            try{

                JsonObject object = new JsonObject(dataPoint.getEdgeJson());
                String reverseFormula = object.getJsonObject("extRule").getString("reverseFormula");
                dataPoint.setReverseFormula(reverseFormula);
            }catch (Exception e){
                e.printStackTrace();
                log.error("反向公式解析失败：",e);
            }
        }

        return dataPoint;
    }


    /**
     * 获取用户信息   根据用户ID
     *
     * @param userId
     * @return
     */
    @Override
    public UserInfo getUserInfoByUserId(long userId) {
        return userAlarmMapper.getUserInfoByUserId(userId);
    }

    /**
     * 获取分享用户信息列表 根据设备编号
     * @param deviceNo
     * @return
     */
    @Override
    public List<UserInfo> getShareUserInfo(String deviceNo) {
        return userAlarmMapper.getDevShareUserInfo(deviceNo);
    }

    /**
     * 插入新的报警历史记录
     *
     * @param alarmHistory
     * @return
     */
    @Override
    public Integer insertAlarmHistory(AlarmHistory alarmHistory) {
        return triggerAlarmMapper.insertAlarmHistory(alarmHistory);
    }


}
