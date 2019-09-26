package cn.usr.cloud.alarm.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 赵震
 * @date 2018-09-12 17:22
 */
@Data
public class AlarmContactsDTO implements Serializable {

    /**
     * 报警联系人名称
     */
    private String contactsName;


    /**
     * 邮箱地址
     */
    private String email;


    /**
     * 微信绑定id，详见iot_u2wx_bind表
     */
    private String openId;


    /**
     * 手机号
     */
    private String tel;

}
