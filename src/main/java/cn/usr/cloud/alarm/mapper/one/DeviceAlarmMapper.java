package cn.usr.cloud.alarm.mapper.one;

import cn.usr.cloud.alarm.entity.DeviceInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * @author 赵震
 * @date 2018-09-11 16:40
 */
@Mapper
@Repository
public interface DeviceAlarmMapper {

    /**
     * 获取设备信息 根据设备ID
     *
     * @param deviceId
     * @return
     */
    @Select("select id, add_uid, devid, sn, name, pass, type, hardver, softver, img, protocol, template_id, " +
            "address, polling_interval, product_model, weight , verify_code  " +
            " from iot_device " +
            " where devid = #{deviceId}")
    @Results({
            @Result(column = "add_uid", property = "userId"),
            @Result(column = "devid", property = "deviceNo"),
            @Result(column = "template_id", property = "templateId"),
            @Result(column = "polling_interval", property = "pollingInterval"),
            @Result(column = "product_model", property = "productModel"),
    })
    DeviceInfo getDeviceInfoByDeviceId(@Param("deviceId") String deviceId);
}
