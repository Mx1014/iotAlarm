package cn.usr.cloud.alarm.mapper.one;

import cn.usr.cloud.alarm.dto.AlarmContactsDTO;
import cn.usr.cloud.alarm.entity.AlarmHistory;
import cn.usr.cloud.alarm.entity.Linkage;
import cn.usr.cloud.alarm.entity.Trigger;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AlarmMapper {

    /**
     * 只返回开启的触发器
     * @param dataPointTemplateId
     * @param userId
     * @param value
     * @param deviceId
     * @return
     */
    @Select(" select id,uid,trigger_name,dataid,trigger_condition,alarm_mode,insert_type,max,min,status," +
            "custom_content_alarm,custom_content_normal,type,slave_index, " +
            " #{value} as inputValue, #{deviceId} as deviceId " +
            " from iot_alarm_trigger where dataid = #{dataPointTplId} AND status=1 AND uid=#{uid}" +
            " limit 1")

    @Results({
            @Result(column = "uid", property = "userId"),
            @Result(column = "trigger_name", property = "triggerName"),
            @Result(column = "dataid", property = "dataPointTemplateId"),
            @Result(column = "trigger_condition", property = "condition"),
            @Result(column = "alarm_mode", property = "alarmMode"),
            @Result(column = "insert_type", property = "insertType"),
            @Result(column = "custom_content_alarm", property = "alarmContent"),
            @Result(column = "custom_content_normal", property = "recoverContent"),
            @Result(column = "slave_index", property = "slaveIndex"),
    })
    Trigger getTriggerByDataPointTemplateId(@Param("dataPointTplId") Integer dataPointTemplateId,
                                            @Param("uid") Integer userId,
                                            @Param("value") Double value,
                                            @Param("deviceId") String deviceId
    );

    /**
     * 只返回开启的触发器
     * @param dataPointTemplateId
     * @param relId
     * @return
     */
    @Select("SELECT id,uid,trigger_name,dataid,trigger_condition,alarm_mode,insert_type,max,min,status," +
            "   custom_content_alarm,custom_content_normal,type,slave_index, linkage_control_status " +
            "FROM iot_alarm_trigger WHERE (type=0 AND dataid=#{dataPointTemplateId} AND status = 1) OR (type=1 AND dataid = #{relId} AND status = 1)")
    @Results({
            @Result(column = "uid", property = "userId"),
            @Result(column = "dataid", property = "dataPointTemplateId"),
            @Result(column = "trigger_condition", property = "condition"),
            @Result(column = "custom_content_alarm", property = "alarmContent"),
            @Result(column = "custom_content_normal", property = "recoverContent")
    })
    List<Trigger> getTriggerByDataPoint(@Param("dataPointTemplateId") Integer dataPointTemplateId, @Param("relId") Integer relId);

    /**
     * 插入新的报警记录
     * @param alarmHistory
     * @return
     */
    @Insert({
            "<script>" +
                    "insert into iot_alarm_history (did, dataid, value, content, create_time, generate_time, alarm_state, slave_index, triggerid, uid " +
                    "<if test='linkageControlStatus != null'> ,linkage_control_status </if>" +
                    "<if test='controlType != null'> ,control_type </if>" +
                    "<if test='controlTargetType != null'> ,control_target_type </if>" +
                    "<if test='controlDeviceId != null'> ,control_device_id </if>" +
                    "<if test='controlSlaveIndex != null'> ,control_slave_index </if>" +
                    "<if test='controlTemplateId != null'> ,control_template_id </if>" +
                    "<if test='controlDataPointId != null'> ,control_data_id </if>" +
                    "<if test='controlSendData != null'> ,control_send_data </if> )" +
                    "values (#{deviceNo}, #{dataPointTemplateId}, #{value}, #{content}, unix_timestamp(now()), #{generateTime}, #{alarmState}, #{slaveIndex}, #{triggerId}, #{userId}" +
                    "<if test='linkageControlStatus != null'> ,#{linkageControlStatus} </if>" +
                    "<if test='controlType != null'>,#{controlType}</if>" +
                    "<if test='controlTargetType != null'>,#{controlTargetType}</if>" +
                    "<if test='controlDeviceId != null'>,#{controlDeviceId}</if>" +
                    "<if test='controlSlaveIndex != null'>,#{controlSlaveIndex}</if>" +
                    "<if test='controlTemplateId != null'>,#{controlTemplateId}</if>" +
                    "<if test='controlDataPointId != null'>,#{controlDataPointId}</if>" +
                    "<if test='controlSendData != null'>,#{controlSendData}</if> )" +
                    "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn="id")
    Integer insertAlarmHistory(AlarmHistory alarmHistory);

    /**
     * 获取触发器对应的报警联系人 对应记录id
     * @param triggerId
     * @return
     */
    @Select("select contact_id from iot_alarm_contacts where trigger_id=#{triggerId} and contact_type=0")
    List<Integer> getContactSingleIdListByTriggerId(@Param("triggerId") Integer triggerId);

    @Select("select name, phoneNumber as tel, email,wx_id from iot_alarm_contact_user where id=#{id}")
    AlarmContactsDTO getAlarmContactsById(@Param("id") Integer id);

    @Select("SELECT NAME,phoneNumber AS tel,email,openid_official_accounts FROM iot_alarm_contact_user u LEFT JOIN iot_alarm_contacts c ON c.contact_id=u.id LEFT JOIN iot_u2wx_bind wx ON wx.id=u.wx_id WHERE c.trigger_id=#{triggerId} AND contact_type=0")
    @Results({
            @Result(column = "openid_official_accounts", property = "openId")
    })
    List<AlarmContactsDTO> getAlarmContactsByTriggerId(Integer triggerId);

    @Select("select openid_official_accounts from iot_u2wx_bind where id=#{id}")
    String getWeChatOpenIdById(@Param("id") Integer id);

    @Select("select id, uid,trigger_id,control_type,control_target_type,device_id,slave_index, template_id, data_id, send_data " +
            " from iot_linkage_control where trigger_id=#{triggerId} limit 1")
    @Results({
            @Result(column = "trigger_id", property = "triggerId"),
            @Result(column = "control_type", property = "type"),
            @Result(column = "device_id", property = "deviceId"),
            @Result(column = "slave_index", property = "slaveIndex"),
            @Result(column = "data_id", property = "dataPointTemplateId"),
    })
    Linkage getLinkageByTriggerId(@Param("triggerId") Integer triggerId);
}
