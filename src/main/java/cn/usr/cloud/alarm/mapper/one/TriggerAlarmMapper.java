package cn.usr.cloud.alarm.mapper.one;

import cn.usr.cloud.alarm.entity.AlarmHistory;
import cn.usr.cloud.alarm.entity.Trigger;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 赵震
 * @date 2018-09-12 15:02
 */
@Mapper
@Repository
public interface TriggerAlarmMapper {

    /**
     * 插入新的报警记录
     * @param alarmHistory
     * @return
     */
    @Insert("INSERT INTO alarm_history (device_no, data_point_id, value, content, generate_time, alarm_state, " +
            "status, slave_index, user_id, triggerId, create_dt) VALUES (#{deviceNo}, #{dataPointTemplateId}, #{value}, " +
            "#{content}, #{generateTime}, #{alarmState}, #{status}, #{slaveIndex}, #{userId}, #{triggerId}, now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insertAlarmHistory(AlarmHistory alarmHistory);

    /**
     * 根据数据点真实id获取所对应此设备和此数据点模板的触发器
     * @param dataPointId
     * @return
     */
    @Select("SELECT iot_alarm_trigger.id,uid,trigger_name,dataid,trigger_condition,alarm_mode,insert_type,max,min,STATUS,custom_content_alarm,custom_content_normal,dead_zone,type,slave_index,linkage_control_status " +
            "FROM iot_alarm_trigger " +
            "LEFT JOIN device_template_point_rel rel " +
            "ON rel.id=#{dataPointId} " +
            "WHERE (type=1 AND dataid=rel.device_template_point_id AND STATUS=1) " +
            "OR (type=2 AND dataid=#{dataPointId} AND STATUS=1)")
    @Results({
            @Result(column = "uid", property = "userId"),
            @Result(column = "dataid", property = "dataPointTemplateId"),
            @Result(column = "trigger_condition", property = "condition"),
            @Result(column = "custom_content_alarm", property = "alarmContent"),
            @Result(column = "custom_content_normal", property = "recoverContent")
    })
    List<Trigger> getTriggerByDataPointId(Integer dataPointId);

    /**
     * 根据数据点真实id获取所对应此设备和此数据点模板的触发器
     * @param triggerId
     * @return
     */
    @Select("SELECT id, dataid,type " +
            "FROM iot_alarm_trigger " +
            "WHERE id = #{triggerId}")
    @Results({
            @Result(column = "dataid", property = "dataPointTemplateId")
    })
    Trigger getTriggerById(Integer triggerId);
}
