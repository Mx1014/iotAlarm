package cn.usr.cloud.alarm.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author 赵震
 * @date 2018-09-11 11:51
 */
@Data
public class UserInfo {

    private Integer id;

    private String account;

    private String realName;

    private String email;

    private String tel;

    private Integer vipLevel;

    private Integer autoWorkorder;

    private Integer projectId;

    private Integer userType;

    private Integer userLevel;
}
