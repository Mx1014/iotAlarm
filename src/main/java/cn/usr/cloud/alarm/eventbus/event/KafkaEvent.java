package cn.usr.cloud.alarm.eventbus.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@Data
@ToString
public class KafkaEvent {
    public String topic;
    public String moduleName;
    public String msg;
}
