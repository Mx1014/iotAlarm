package cn.usr.cloud.alarm.util;

import cn.usr.cloud.alarm.dto.AlarmInfoDTO;
import cn.usr.cloud.alarm.entity.Trigger;

import java.text.SimpleDateFormat;
import java.util.Date;

import static cn.usr.cloud.alarm.constant.ConstanTriggerCondition.*;

/**
 * @Package: cn.usr.alarm.broadcast.service
 * @Description: TODO
 * @author: Rock 【shizhiyuan@usr.cn】
 * @Date: 2018/3/13 0013 15:23
 */
public class AlarmUtil {
    /**
     * 组织触发条件语言
     *
     * @return
     */
    public static String getTriggerCondition(Trigger trigger, boolean alarmState) {

        if (!alarmState) {
            //TODO 判断是否自定义了恢复正常推送内容
            return "已恢复正常状态";
        }
        //如果有小数点就显示小数点，如果没有就不显示
        Double doubV = trigger.getInputValue();
        String value = (doubV % 1) != 0 ? doubV.toString() : String.valueOf(doubV.intValue());
        switch (trigger.getCondition()) {
            case SWITCH_OFF:
                return "当前值：" + value + "，不符合您设置的关闭状态";
            case SWITCH_ON:
                return "当前值：" + value + "，不符合您设置的开启状态";
            case LESS:
                return "当前值：" + value + "，已低于您设置的" + trigger.getMin() + "值";
            case GREATER:
                return "当前值：" + value + "，已高于您设置的" + trigger.getMax() + "值";
            case BETWEEN:
                return "当前值：" + value + "，已介于您设置的" + trigger.getMax() + "值 和" + trigger.getMin() + "值之间";
            case OUT_BETWEEN:
                return "当前值：" + value + "，已高于您设置的" + trigger.getMax() + " 值" + "或已低于你设置的" + trigger.getMin() + "值";
            case EQUAL:
                return "当前值：" + value + "，已等于您设置的" + trigger.getMin() + " 值";
            default:
                return "";
        }
    }


    /**
     * 获取报警描述内容
     *
     * @param alarmInfoDto
     * @return
     */
    public static String getAlarmContent(AlarmInfoDTO alarmInfoDto) {

        /*if (alarmInfoDto.getAlarmState() == 0) {

            // 报警状态为 正常状态
            return "恢复正常状态";

        } else {

            // 报警状态为 报警状态
            switch (alarmInfoDto.getTriggerInfo().getTriggerCondition()) {
                case SWITCH_OFF:
                    return "开关OFF";
                case SWITCH_ON:
                    return "开关ON";
                case LESS:
                    return "低于" + alarmInfoDto.getTriggerInfo().getMin();
                case GREATER:
                    return "高于" + alarmInfoDto.getTriggerInfo().getMax();
                case BETWEEN:
                    return "处于" + alarmInfoDto.getTriggerInfo().getMax()
                            + "和" + alarmInfoDto.getTriggerInfo().getMin() + "之间";
                case OUT_BETWEEN:
                    return "高于" + alarmInfoDto.getTriggerInfo().getMax()
                            + "或低于" + alarmInfoDto.getTriggerInfo().getMin();
                case EQUAL:
                    return "等于" + alarmInfoDto.getTriggerInfo().getMin();
                default:
                    return "other";
            }
        }*/
        return null;
    }

    /**
     * 组织触发条件语言 - 概述
     *
     * @param alarmInfoDTO
     * @return
     */
    public static String getTriggerConditionSummary(final AlarmInfoDTO alarmInfoDTO) {
        if (!alarmInfoDTO.isAlarmState()) {
            return "已恢复正常状态";
        }
        Trigger trigger = alarmInfoDTO.getTrigger();

        switch (trigger.getCondition()) {
            case SWITCH_OFF:
                return "不符合您设置的关闭状态";
            case SWITCH_ON:
                return "不符合您设置的开启状态";
            case LESS:
                return "当前值已低于您设置的阈值";
            case GREATER:
                return "当前值已高于您设置的阈值";
            case BETWEEN:
                return "当前值已介于您设置的阈值之间";
            case OUT_BETWEEN:
                return "当前值已超出或低于您设置的阈值区间";
            default:
                return "";
        }
    }

    /**
     * 转换时间类I型   毫秒
     */
    public static String milliSecondForDate(Long millisecond) {
        Date date;
        if (millisecond == null) {
            date = new Date();
        } else {
            date = new Date(millisecond);
        }
        // 24小时制
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        return format.format(date);
    }

    /**
     * 截取字符串，超出长度则将限定长度的最后两个字符修改为".."
     */
    public static String subStringLength(String subStr, int maxLength) {
        if (subStr.length() > maxLength) {
            subStr = subStr.substring(0, maxLength - 2) + "..";
        }

        return subStr;
    }

}
