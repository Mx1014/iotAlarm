package cn.usr.cloud.alarm.ignite;

import cn.usr.cloud.alarm.entity.DeviceStatus;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;


/**
 * @Auther: Andy(李永强)
 * @Date: 2019/6/24 上午11:07
 * @Describe:
 */
@Component
public interface DeviceStatusMapper {
    @Select("select DEVICE_ID, alarm from DEVICE_STATUS where DEVICE_ID = #{deviceId}")
    @Results({
            @Result(column = "device_id", property = "deviceId")
    })
    DeviceStatus getDeviceStatus(String deviceId);

    @Insert("insert into DEVICE_STATUS (DEVICE_ID, ALARM) values (#{deviceId}, #{alarm})")
    int insertDeviceStatus(DeviceStatus deviceStatus);

    @Update("update DEVICE_STATUS set ALARM=#{alarm} where DEVICE_ID = #{deviceId}")
    int updateDeviceStatus(DeviceStatus deviceStatus);
}
