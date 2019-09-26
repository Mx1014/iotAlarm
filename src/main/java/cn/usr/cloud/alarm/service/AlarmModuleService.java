package cn.usr.cloud.alarm.service;


import cn.usr.cloud.alarm.entity.AlarmHistory;
import cn.usr.cloud.alarm.entity.DataPointInfo;
import cn.usr.cloud.alarm.entity.DeviceInfo;
import cn.usr.cloud.alarm.entity.UserInfo;

import java.util.List;

/**
 * @author 赵震
 * @date 2018-09-12 14:42
 */
public interface AlarmModuleService {

    /**
     * 获取设备信息 根据设备ID
     * @param deviceId
     * @return
     */
    DeviceInfo getDeviceInfoByDeviceId(String deviceId);


    /**
     * 获取数据点信息  根据数据点ID
     * @param dataPointId
     * @return
     */
    DataPointInfo getDataPointInfoByDataPointId(long dataPointId);

    /**
     * 获取用户信息   根据用户ID
     * @param userId
     * @return
     */
    UserInfo getUserInfoByUserId(long userId);

    /**
     * 获取分享用户信息列表 根据设备编号
     * @param deviceNo
     * @return
     */
    List<UserInfo> getShareUserInfo(String deviceNo);

    /**
     * 插入新的报警历史记录
     * @param alarmHistory
     * @return
     */
    Integer insertAlarmHistory(AlarmHistory alarmHistory);


}
