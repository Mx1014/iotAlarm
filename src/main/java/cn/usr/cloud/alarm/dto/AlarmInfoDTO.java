package cn.usr.cloud.alarm.dto;

import cn.usr.cloud.alarm.entity.*;
import lombok.Data;

/**
 * @author 赵震
 * @date 2018-09-10 18:40
 *
 * 报警信息集合
 */
@Data
public class AlarmInfoDTO {

    /**
     * 设备信息
     */
    private DeviceInfo deviceInfo;

    /**
     * 从机信息
     */
    private SlaveInfo slaveInfo;

    /**
     * 数据点信息
     */
    private DataPointInfo dataPointInfo;

    /**
     * 数据点信息
     */
//    private DataPointTemplate dataPointTemplate;

    /**
     * 数据点的真实ID
     */
    private Integer dataPointRelId;

    /**
     * 触发器信息
     */
    private Trigger trigger;

    /**
     * 用户信息
     */
    private UserInfo userInfo;

    /**
     * 当前值
     */
    private Double value;

    /**
     * 触发状态 未触发:0 触发:1
     */
    private boolean alarmState;

    /**
     * 报警时间
     */
    private Long alarmTime;

    /**
     * 报警次数
     */
    private int sendCount;

}
