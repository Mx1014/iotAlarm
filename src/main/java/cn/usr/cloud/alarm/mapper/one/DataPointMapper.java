package cn.usr.cloud.alarm.mapper.one;

import cn.usr.cloud.alarm.dto.TargetDataPointDTO;
import cn.usr.cloud.alarm.entity.DataPoint;
import cn.usr.cloud.alarm.entity.DataPointInfo;
import cn.usr.cloud.alarm.entity.DataPointTemplate;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lzn
 * @version 0.0.1
 * @update 2018-09-16
 */
@Mapper
@Repository
public interface DataPointMapper {

    @Select("SELECT device_no, id from device_template_point_rel where device_template_point_id = #{dataPointTemplateId} ")
    @Results({
            @Result(column = "device_no", property = "deviceId"),
            @Result(column = "id", property = "targetDataPoingId")
    })
    List<TargetDataPointDTO> getDataPointListByDataPointTemplateId(@Param("dataPointTemplateId") Integer dataPointTemplateId);

    /**
     * 关联数据库 device_template_point_info
     * @param tplId
     * @return
     */
    @Select("select id, name, edge_json as edgeJson, item_id as itemId, device_template_slave_id as slaveId " +
            " from device_template_point_info " +
            "where id = #{tplId}")
    DataPointTemplate getDataPointTemplate(@Param("tplId") Integer tplId);


    @Insert({ "insert into iot_data_history(dataid, value, create_time, did, slave_index, uid,alarm_his_id,alarm,generate_time) " +
            " values(#{dataId}, #{value}, #{t}, #{deviceId}, #{slaveIndex}, #{userId},0,0,#{generateTime})" })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertDataRecord(@Param("dataId") Integer dataPointId,
                         @Param("value") String value,
                         @Param("t") String t,
                         @Param("deviceId") String deviceId,
                         @Param("slaveIndex") String slaveIndex,
                         @Param("userId") Integer ownerId,
                         @Param("generateTime") Long generateTimestampSecond
    );
    /**
     * 关联数据库 device_template_point_rel
     * @param dataPointId
     * @return
     */
    @Select("select id, user_id as userId, device_no as deviceId, device_template_point_id as dataPointTemplateId " +
            "from  device_template_point_rel where id = #{id}")
    DataPoint getDataPointTemplateRefById(@Param("id") Integer dataPointId);

    /**
     * 关联数据库 device_template_point_rel
     * @param deviceId
     * @param dataPointTemplateId
     * @return
     */
    @Select("select id from device_template_point_rel where device_no = #{deviceId} and device_template_point_id = #{dataPointTemplateId}")
    Integer getDataPointRelIdByDeviceIdDataPointTemplateId(@Param("deviceId") String deviceId, @Param("dataPointTemplateId") Integer dataPointTemplateId);

    /**
     * 关联数据库 device_template_point_rel
     * @param dataPointTemplateId
     * @return
     */
    @Select("select id, user_id, device_template_slave_id, name, item_id, edge_json " +
            "from device_template_point_info where id = #{id}")
    @Results({
            @Result(column = "device_template_slave_id", property = "slaveId")
    })
    DataPointInfo getDataPointInfoById(@Param("id") Integer dataPointTemplateId);

    @Select("SELECT info.id,info.user_id,info.device_template_slave_id,info.NAME,info.item_id,info.edge_json " +
            "FROM device_template_point_info info " +
            "RIGHT JOIN device_template_point_rel rel " +
            "ON info.id=rel.device_template_point_id " +
            "WHERE rel.id=#{dataPointId}")
    @Results({
            @Result(column = "device_template_slave_id", property = "slaveId"),
            @Result(column = "edge_json", property = "edgeJson")
    })
    DataPointInfo getDataPointInfoByDataPointId(@Param("dataPointId") Integer dataPointId);

    /**
     * 根据设备模板id,获取与之关联的数据点真实id
     * @param deviceTemplateId
     * @return
     */
    @Select("SELECT rel.id " +
            "FROM device_template_point_info point " +
            "RIGHT JOIN device_template_slave_info SLAVE ON point.device_template_slave_id=SLAVE.id " +
            "RIGHT JOIN device_template_info template ON SLAVE.device_template_id=template.id " +
            "RIGHT JOIN device_template_point_rel rel ON point.id=rel.device_template_point_id " +
            "WHERE template.id=#{deviceTemplateId}")

    List<Integer> getDataPointRelIdsByDeviceTemplateId(Integer deviceTemplateId);

    /**
     * 根据设备模板id,获取与之关联的数据模板id
     * @param deviceTemplateId
     * @return
     */
    @Select("SELECT point.id " +
            "FROM device_template_point_info point " +
            "LEFT JOIN device_template_slave_info slave ON point.device_template_slave_id = slave.id " +
            "LEFT JOIN device_template_info template ON slave.device_template_id = template.id " +
            "WHERE template.id=#{deviceTemplateId}")

    List<Integer> getDataPointTemplateIdsByDeviceTemplateId(Integer deviceTemplateId);

    /**
     * 根据设备模板id,获取与之关联的数据点真实id
     * @param dataPointTemplateId
     * @return
     */
    @Select("SELECT rel.id " +
            "FROM device_template_point_info point " +
            "RIGHT JOIN device_template_point_rel rel ON point.id=rel.device_template_point_id " +
            "WHERE point.id = #{dataPointTemplateId}")
    List<Integer> getDataPointRelIdsByDataPointTemplateId(Integer dataPointTemplateId);

    /**
     * 根据deviceNo,获取与之关联的数据点真实id
     * @param deviceNo
     * @return
     */
    @Select("SELECT id " +
            "FROM device_template_point_rel " +
            "WHERE device_no = #{deviceNo}")
    List<Integer> getDataPointRelIdsByDeviceNo(String deviceNo);
}
