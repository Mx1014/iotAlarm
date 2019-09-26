package cn.usr.cloud.alarm.mapper.two;

import cn.usr.cloud.alarm.entity.AlarmHistory;
import org.apache.ibatis.annotations.*;


@Mapper
public interface AlarmInsertMapper {

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
}
