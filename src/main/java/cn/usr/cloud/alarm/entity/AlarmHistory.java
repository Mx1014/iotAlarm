package cn.usr.cloud.alarm.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author 赵震
 * @date 2018-09-13 08:41
 */
@Data
public class AlarmHistory {

    private Long id;

    private String deviceNo;

    private Integer dataPointTemplateId; //TODO 改为dataPointTemplateId

    private Double value;

    private String content;

    private Long generateTime;

    private Integer alarmState;

    private Integer status;

    private String description;

    private Long processTime;

    private String slaveIndex;

    private String handleMode;

    private Integer handle;

    private Integer userId;

    private Integer triggerId;

    private Integer linkageControlStatus;

    private Integer controlType;

    private Integer controlTargetType;

    private String controlDeviceId;

    private String controlSlaveIndex;

    private Integer controlTemplateId;

    private Integer controlDataPointId;

    private String controlSendData;

    private Date createDt;

}
