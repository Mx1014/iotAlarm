package cn.usr.cloud.alarm.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author 赵震
 * @date 2018-09-11 11:40
 */
@Data
public class DataPointInfo {

    private Integer id;

    private Integer userId;

    private Integer slaveId;

    private String itemId;

    private String name;

    private String edgeJson;

    private String unit;

    private String formula;

    private Integer decimalAccuracy;

    private String description;

    private String reverseFormula;

    private Integer reverseDecimalAccuracy;
}
