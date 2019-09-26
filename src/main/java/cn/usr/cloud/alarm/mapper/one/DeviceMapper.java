package cn.usr.cloud.alarm.mapper.one;

import cn.usr.cloud.alarm.entity.DeviceInfo;
import org.apache.ibatis.annotations.*;

import java.util.Set;

/**
 * Created by pi on 2018/9/10.
 */
@Mapper
public interface DeviceMapper {

    /**
     * 使用设备id查询设备名，新权限体系
     *
     */
    /*@Select("select id, devid, name, type, address, owner_uid, template_id, project_id from iot_device where devid = #{deviceId}")
    @Results({
            @Result(column = "devid", property = "deviceNo")
    })
    DeviceInfo getDeviceByDeviceId(@Param("deviceId") String deviceId);*/

    /**
     * 老权限体系
     * @param deviceId
     * @return
     */
    @Select("SELECT iot_device.id,iot_device.devid,iot_device.name,iot_device.type,iot_device.address,iot_u2d.uid, iot_device.template_id, iot_device.project_id " +
            "FROM iot_device " +
            "LEFT JOIN iot_u2d ON iot_device.devid=iot_u2d.did " +
            "WHERE iot_device.devid= #{deviceId}")
    @Results({
            @Result(column="devid", property="deviceNo"),
            @Result(column="uid", property="ownerUid")
    })
    DeviceInfo getDeviceByDeviceId(@Param("deviceId") String deviceId);

    /**
     * 使用设备id查询所属用户
     */
    @Select("select add_uid from iot_device where devid = #{deviceId}")
    Long getUserIdByDeviceId(@Param("deviceId") String deviceId);

    @Select("select dna from device_template_info where id = #{tplId}")
    Integer getDnaByDeviceTemplateId(@Param("tplId") Integer tplId);

    /**
     * 从**数据库**中获取在线设备集合。
     * TODO 注意！！！ SQL语句有性能瓶颈
     * 该查询只在程序第一次启动时运行。
     * @return
     */
    @Select("SELECT did FROM iot_online_history \n" +
            "WHERE \n" +
            "    id IN (\n" +
            "        SELECT MAX(id) FROM iot_online_history GROUP BY did \n" +
            "    ) " +
            " AND state = 1 ")
    Set<String> getOnlineDeviceSet();


    /**
     * 根据报警数据点查询报警设备集合
     * @param dataPointIds
     * @return
     */
    @Select("<script> " +
            " SELECT DISTINCT device_no FROM device_template_point_rel WHERE " +
            " id IN " +
            " <foreach item='item' index='index' collection='dataPointIds' open='(' separator=',' close=')'> #{item} </foreach> " +
            "</script>")
    Set<String> getAlarmDeiceSetByDataPointIds(@Param("dataPointIds") Set<Long> dataPointIds);

    @Select("select id from device_template_point_rel where device_no = #{deviceId}")
    Set<Long> getDataPointIdsByDeviceId(@Param("deviceId") String deviceId);
}
