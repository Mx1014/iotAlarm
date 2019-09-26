package cn.usr.cloud.alarm.mapper.one;

import cn.usr.cloud.alarm.entity.SlaveInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.sql.Date;
import java.util.List;

/**
 * @author 赵震
 * @date 2018-09-11 17:09
 */
@Mapper
public interface SlaveMapper {

    /**
     * 获取设备拥有的从机列表  根据设备编号
     * @return
     */
    @Select("select id, user_id, slave_index, slave_name, slave_addr, device_template_id " +
            "from device_template_slave_info where device_template_id = #{tplId} and slave_index = #{slaveIndex} limit 1")
    SlaveInfo getSlaveByTplIdAndSalveIndex(@Param("tplId") Integer tplId, @Param("slaveIndex") String slaveIndex);

    /**
     * 获取设备拥有的从机列表  根据设备编号
     * @return
     */
    @Select("select id, user_id, slave_index, slave_name, slave_addr, device_template_id " +
            "from device_template_slave_info where device_template_id = #{tplId}")
    @Results({
            @Result(column = "user_id", property = "userId"),
            @Result(column = "slave_index", property = "slaveIndex"),
            @Result(column = "slave_name", property = "slaveName"),
            @Result(column = "slave_addr", property = "slaveAddr"),
            @Result(column = "data_template_id", property = "dataTemplateId")
    })
    List<SlaveInfo> getSlaveListBytemplateId(@Param("tplId") Integer tplId);

}
