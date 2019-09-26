package cn.usr.cloud.alarm.dto;


import lombok.Data;

import java.io.Serializable;


/**
 * @Package: cn.usr.cloud.db.dto
 * @Description: 中性微信号配置信息
 * @author: Rock 【shizhiyuan@usr.cn】
 * @Date: 2018/3/8 0008 09:15
 */
@Data
public class NeutralWpInfoDTO implements Serializable {

    /**
     * 用户ID
     */
    private Integer userId;



    /**
     * 微信公众号ID
     */
    private String mpAppId;

    /**
     * 微信公众号秘钥
     */
    private String mpSecret;

    /**
     * 微信公众号模板ID
     */
    private String mpTemplateId;


}
