package cn.usr.cloud.alarm.entity;

import lombok.Data;

import java.io.Serializable;

import static cn.usr.cloud.alarm.constant.ConstanTriggerCondition.*;

/**
 * Created by pi on 2018/10/11.
 */
@Data
public class Trigger implements Serializable {
    Integer id;
    Integer userId;
    String triggerName;
    String deviceId;
    Integer dataPointTemplateId;//TODO 改为DataPointTemplateId
    Integer condition;
    String alarmMode;
    Integer insertType;
    Double max;
    Double min;
    Integer status;
    String alarmContent;
    String recoverContent;
    String slaveIndex;
    Integer type;
    Double deadZone;

    int linkageControlStatus;

    Double inputValue;

    /**
     * 检查参数是否满足触发器条件，满足条件时返回true，否则返回false
     * （计算死区的情况下）
     *
     * @Param alarmStatus 当前是否在报警状态
     * * @return
     */
    public Boolean isSufficientDeadZoneValue(boolean alarmStatus) {
        deadZone = deadZone == null ? 0 : deadZone;
        Double tempDeadZone = alarmStatus ? -deadZone : deadZone;

        switch (condition) {
            case SWITCH_OFF: // 开关OFF(0)
                return inputValue < 0.5;
            case SWITCH_ON: //开关ON(1)
                return inputValue > 0.5;
            case LESS: //数值低于(2)
                return inputValue < (min - tempDeadZone);
            case GREATER: //数值高于(3)
                return inputValue > (max + tempDeadZone);
            case BETWEEN: //数值介于(4)
                return (inputValue >= (min + tempDeadZone)) && (inputValue <= (max - tempDeadZone));
            case OUT_BETWEEN: //数值高于max低于min(5)
                return (inputValue < (min - tempDeadZone)) || (inputValue > (max + tempDeadZone));
            case EQUAL: //数值等于min(6)
                return inputValue >= min - 0.1 && inputValue <= min + 0.1;
        }
        return false;
    }

    /**
     * 检查参数是否满足触发器条件，满足条件时返回true，否则返回false
     * （不计算死区的情况下）
     *
     * @return
     */
    public Boolean isSufficientValue() {

        switch (condition) {
            case SWITCH_OFF: // 开关OFF(0)
                return inputValue < 0.5;
            case SWITCH_ON: //开关ON(1)
                return inputValue > 0.5;
            case LESS: //数值低于(2)
                return inputValue < min;
            case GREATER: //数值高于(3)
                return inputValue > max;
            case BETWEEN: //数值介于(4)
                return (inputValue >= min) && (inputValue <= max);
            case OUT_BETWEEN: //数值高于max低于min(5)
                return (inputValue >= max) || (inputValue <= min);
            case EQUAL: //数值等于min(6)
                return inputValue >= min - 0.1 && inputValue <= min + 0.1;
        }
        return false;
    }
}
