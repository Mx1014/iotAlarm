package cn.usr.cloud.alarm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * @Auther: Andy(李永强)
 * @Date: 2019/6/24 上午11:51
 * @Describe: Iginte对象
 */
@Data
@ToString
@AllArgsConstructor
public class DeviceStatus {
    private String deviceId;

    private Integer alarm;
}
