package cn.usr.cloud.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Auther: Andy(李永强)
 * @Date: 2019/5/20 下午12:23
 * @Describe:
 */

@AllArgsConstructor
@Data
public class TargetDataPointDTO {
    String deviceId;
    Integer targetDataPoingId;
}
