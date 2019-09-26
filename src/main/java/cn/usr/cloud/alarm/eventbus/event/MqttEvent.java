package cn.usr.cloud.alarm.eventbus.event;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MqttEvent {
    public String topic;
    public String moduleName;
    public String msg;
}
