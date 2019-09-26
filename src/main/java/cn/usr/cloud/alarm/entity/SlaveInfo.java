package cn.usr.cloud.alarm.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author 赵震
 * @date 2018-09-11 11:37
 */
@Data
public class SlaveInfo {

    private Integer id;

    private Integer userId;

    private Integer deviceTemplateId;

    private String slaveIndex;

    private String slaveName;

    private String slaveAddr;

}
