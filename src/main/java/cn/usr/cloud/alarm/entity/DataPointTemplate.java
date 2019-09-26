package cn.usr.cloud.alarm.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;

/**
 * Created by pi on 2018/9/27.
 */
@Data
@Slf4j
public class DataPointTemplate extends BaseEntity {
    String name;
    String itemId;
    String edgeJson;
    String slaveId;
    Timestamp updateDt;
}
