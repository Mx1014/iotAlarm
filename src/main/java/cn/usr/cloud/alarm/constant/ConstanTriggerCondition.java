package cn.usr.cloud.alarm.constant;

/**
 * @Auther: Andy(李永强)
 * @Date: 2019/6/5 上午11:35
 * @Describe:
 */
public class ConstanTriggerCondition {

    /**
     * 关闭
     */
    public static final int SWITCH_OFF = 0;
    /**
     * 开启
     */
    public static final int SWITCH_ON = 1;
    /**
     * 小于
     */
    public static final int LESS = 2;

    /**
     * 大于
     */
    public static final int GREATER = 3;

    /**
     * 不介于
     */
    public static final int BETWEEN = 4;

    /**
     * 数值高于max低于min
     */
    public static final int OUT_BETWEEN = 5;

    /**
     * 等于
     */
    public static final int EQUAL = 6;

    public static final int OTHER = 99;
}
