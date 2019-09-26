package cn.usr.cloud.alarm.enums;

import lombok.Getter;
import lombok.ToString;

/**
 * @author zhiyuan
 * Created by liu on 2017-12-07.
 */
@Getter
@ToString
public enum TriggerCondition {
    //触发条件：开关ON(0)、开关OFF(1)、数值低于(2)、数值高于(3)、数值介于(4)、数值高于max低于min(5),等于(6),其他(99)
    SWITCH_OFF(0), SWITCH_ON(1), LESS(2), GREATER(3), BETWEEN(4), OUT_BETWEEN(5), EQUAL(6), OTHER(99);


    private int value;

    TriggerCondition(int value) {
        this.value = value;
    }

    public static TriggerCondition getEnumByValue(Integer value) {
        return null;
    }

    public int code() {
        return this.value;
    }

    public static TriggerCondition get(int index) {
        for (TriggerCondition triggerCondition : TriggerCondition.values()) {
            if (triggerCondition.getValue() == index) {
                return triggerCondition;
            }
        }
        return null;
    }

}
