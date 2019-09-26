package cn.usr.cloud.alarm.mapper.one;

import cn.usr.cloud.alarm.entity.UserInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by pi on 2019-03-15.
 */
@Mapper
public interface UserMapper {
    @Select("select id, account, real_name, email, tel, vip_level, auto_workorder, project_id, usr_user_type, usr_user_level from iot_user where id = #{userId} ")
    @Results({
        @Result(column = "usr_user_type", property = "userType"),
        @Result(column = "usr_user_level", property = "userLevel"),
    })
    UserInfo getUserByUserId(@Param("userId") Long userId);

    @Select("select devid from iot_device where add_uid=#{userId}")
    List<String> getDeviceIdListOfUser(@Param("userId") Long userId);
}
