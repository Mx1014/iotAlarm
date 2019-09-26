package cn.usr.cloud.alarm.mapper.one;

import cn.usr.cloud.alarm.entity.UserInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

/**
 * @author 赵震
 * @date 2018-09-12 15:31
 */
@Mapper
@Repository
public interface UserAlarmMapper {

    /**
     * 获取用户信息   根据用户ID
     * @param userId
     * @return
     */
    @Select("select id, account, real_name, password,    email, " +
            "company, tel, address, remark, type_id  " +
            " from iot_user" +
            " where id = #{userId}")
    @Results({
            @Result(column = "real_name", property = "realName"),
    })
    UserInfo getUserInfoByUserId(@Param("userId") long userId);

    /**
     * 获取分享用户列表 根据设备编号
     * @param deviceNo
     * @return
     */
    @Select("select user_info.id, account, real_name, password, last_login_time, last_login_ip, login_count, " +
            "email, company, tel, address, remark, type_id, callback_address, status, vip_level, auto_workorder, " +
            "locked, default_device_pass, sign_code, user_info.create_dt, update_dt from user_info " +
            "left join device_share_info on device_share_info.user_id = user_info.id where device_no = #{deviceNo}")
    @Results({
            @Result(column = "real_name", property = "realName"),
            @Result(column = "last_login_time", property = "lastLoginTime"),
            @Result(column = "last_login_ip", property = "lastLoginIp"),
            @Result(column = "login_count", property = "loginCount"),
            @Result(column = "type_id", property = "typeId"),
            @Result(column = "callback_address", property = "callbackAddress"),
            @Result(column = "vip_level", property = "vipLevel"),
            @Result(column = "auto_workorder", property = "autoWorkorder"),
            @Result(column = "default_device_pass", property = "defaultDevicePass"),
            @Result(column = "sign_code", property = "signCode"),
            @Result(column = "create_dt", property = "createDt", javaType = Date.class, jdbcType = JdbcType.DATE),
            @Result(column = "update_dt", property = "updateDt", javaType = Date.class, jdbcType = JdbcType.DATE)
    })
    List<UserInfo> getDevShareUserInfo(@Param("deviceNo") String deviceNo);

}
