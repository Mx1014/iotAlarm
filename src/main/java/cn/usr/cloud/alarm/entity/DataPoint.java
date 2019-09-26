package cn.usr.cloud.alarm.entity;

import lombok.Data;

/**
 * Created by pi on 2018/8/23.
 */
@Data
public class DataPoint extends BaseEntity{

    private Integer id;
    private Integer userId;

    private String deviceId;
    private Integer dataPointTemplateId;

}
