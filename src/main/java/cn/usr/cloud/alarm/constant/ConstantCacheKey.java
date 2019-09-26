package cn.usr.cloud.alarm.constant;

/**

 * @author: 李永强
 * @Date: 2019年04月26日17:48:43
 * @Description: Redis key前缀
 */
public class ConstantCacheKey {

    /**
     * 缓存触发器
     */
    public static final String CACHE_TRIGGER = "alarm_trigger";

    /**
     * 缓存联动控制
     */
    public static final String CACHE_LINKAGE = "alarm_linkage";

    /**
     * 缓存数据点模板关联的的所有真实数据点
     */
    public static final String CACHE_DATA_POINT_REL = "alarm_data_point_rel";

    /**
     * 缓存设备
     */
    public static final String CACHE_DEVICE = "alarm_device";

    /**
     * 缓存模板从机
     */
    public static final String CACHE_SLAVE = "alarm_slave";

    /**
     * 缓存设备
     */
    public static final String CACHE_DATA_POINT = "alarm_data_point";

    /**
     * 缓存用户
     */
    public static final String CACHE_USER = "alarm_user";

    /**
     * 缓存设备
     */
    public static final String CACHE_CONTACTS = "alarm_contacts";

    /**
     * 存储正在报警的设备
     */
    public static final String CACHE_ALARM_DEVICE = "alarm_device_alarm:";

    /**
     * 缓存报警记录
     */
    public static final String CACHE_ALARM_RECORD = "alarm_record";


    /**
     * 记录上次保存报警记录的时间
     */
    public static final String CACHE_LAST_SAVE_TIME = "alarm_last_save_time";

    public static final String CACHE_ALARM_RECORD_MYSQL = "alarm_record_mysql";

    public static final String CACHE_LAST_SAVE_TIME_MYSQL = "alarm_last_save_time_mysql";

    /**
     * 缓存数据点模板--【废弃】
     */
    public static final String CACHE_DATA_POINT_TEMPLATE = "alarm_data_point_template";

}
