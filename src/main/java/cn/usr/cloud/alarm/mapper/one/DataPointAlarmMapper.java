package cn.usr.cloud.alarm.mapper.one;

import cn.usr.cloud.alarm.entity.DataPointInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * @author 赵震
 * @date 2018-09-12 14:53
 */
@Mapper
@Repository
public interface DataPointAlarmMapper {

    /**
     * 获取数据点信息  根据数据点ID
     * @param dataPointId
     * @return
     */
    @Select("SELECT info.id,info.user_id,device_template_slave_id, NAME,edge_json " +
            "FROM device_template_point_info info " +
            "LEFT JOIN device_template_point_rel rel ON info.id=rel.device_template_point_id " +
            "WHERE rel.id = #{dataPointId}")
    @Results({
            @Result(column = "user_id", property = "userId"),
            @Result(column = "device_template_slave_id", property = "slaveId"),
            @Result(column = "edge_json", property = "edgeJson"),
    })
    DataPointInfo getDataPointInfoByDataPointId(long dataPointId);

}
