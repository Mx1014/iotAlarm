package cn.usr.cloud.alarm.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author 赵震
 * @date 2018-09-11 10:50
 */
@Data
public class DeviceInfo {

    private Long id;

    private String deviceNo;

    private String name;

    private Integer type;

    private String address;

    private Integer templateId;

    private Integer projectId;

    private Integer ownerUid;

}
