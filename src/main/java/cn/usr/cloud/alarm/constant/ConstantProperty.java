package cn.usr.cloud.alarm.constant;

/**
 * @Package: cn.usr.alarm.broadcast.constant
 * @Description: TODO
 * @author: Rock 【shizhiyuan@usr.cn】
 * @Date: 2018/3/13 0013 10:00
 */
public class ConstantProperty {
    /**
     * 一级用户
     */
    public final static int USERTYPE_ONELEVEL = 1;

    /**
     * 二级用户
     */
    public final static int USERTYPE_TWOLEVEL = 0;

    /**
     * 三级用户
     */
    public final static int USERTYPE_THREELEVEL = 3;

    /**
     * 会员等级：普通用户
     */
    public final static int USERVIPTYPE_NOLEVEL = 0;

    /**
     * 会员等级：一级会员
     */
    public final static int USERVIPTYPE_ONELEVEL = 1;


    /**
     * 有人物联网 微信公众号APPId
     */
    public final static String USR_APPID = "wx0e65a6ece91af37b";

    /**
     * 有人物联网 微信公众号APPSecret
     */
    public final static String USR_APPSECRET = "0a459e08da1f13bf96ffd08c52e91b90";

    /**
     * 有人物联网 微信公众号消息模板Id
     */
    public final static String USR_TEMPLATEID = "ktegt3tnR5Db4YZtX0CgaDJ8hTFC7xZuYJPJkW2pwJo";


    /**
     * 邮件服务器
     */
    public final static String EMAIL_SERVER = "smtp.exmail.qq.com";

    /**
     * 登录邮箱的账号:如 "1974544863@qq.com"
     */
    public final static String LOGIN_ACCOUNT = "cloud@usr.cn";

    /**
     * 登录qq邮箱时候需要的授权码:可以进入qq邮箱,账号设置那里"生成授权码"
     */
    public final static String LOGIN_AUTHCODE = "YouRenYun153";

    /**
     * 发件人
     */
    public final static String SENDER = "cloud@usr.cn";

    /**
     * 邮件的主题
     */
    public final static String EMAIL_SUBJECT = "报警信息";

    /**
     * 邮件内容的类型,支持纯文本:"text/plain;charset=utf-8";,带有Html格式的内容:"text/html;charset=utf-8"
     */
    public final static String EMAIL_CONTENTTYPE = "text/html;charset=utf-8";

    /**
     * 通配符设备ID
     */
    public static final String WILDCARD_DEVID = "%DEVID%";

    /**
     * 通配符设备名称
     */
    public static final String WILDCARD_DEVNAME = "%DEVNAME%";

    /**
     * 通配符数据点ID
     */
    public static final String WILDCARD_DATAPOINTID = "%DATAPOINTID%";

    /**
     * 通配符数据点名称
     */
    public static final String WILDCARD_DATAPOINTNAME = "%DATAPOINTNAME%";

    /**
     * 通配符数据点值
     */
    public static final String WILDCARD_DATAPOINTVALUE = "%NOWVALUE%";

    /**
     * 通配符触发器名称
     */
    public static final String WILDCARD_TRIGGERNAME = "%TRIGGERNAME%";

    /**
     * 通配符触发条件
     */
    public static final String WILDCARD_TRIGGERCONDITION = "%TRIGGERCONDITION%";

    /**
     * 通配符触发时间
     */
    public static final String WILDCARD_TRIGGERTIME = "%TRIGGERTIME%";

    /**
     * 通配符用户名称
     */
    public static final String WILDCARD_USERACCOUNT = "%ACCOUNT%";

    /**
     * 通配符报警联系人名称
     */
    public static final String WILDCARD_ALARMCONTACTSNAME = "%ALARMCONTACTSNAME%";

    /**
     * 通配符从机序号
     */
    public static final String WILDCARD_SLAVEINDEX = "%SLAVEINDEX%";

    /**
     * 通配符从机名称
     */
    public static final String WILDCARD_SLAVENAME = "%SLAVENAME%";

    /**
     * 阿里云短信API产品-名称
     */
    public static final String ALIYUN_SMS_PRODUCT = "Dysmsapi";

    /**
     * 阿里云短信API产品-域名
     */
    public static final String ALIYUN_SMS_DOMAIN = "dysmsapi.aliyuncs.com";

    /**
     * 阿里云短信API产品-有人accessKeyId
     */
    public static final String ALIYUN_ACCESSKEY_ID = "LTAIHXnSt5fnzL0n";

    /**
     * 阿里云短信API产品-有人accessKeySecret
     */
    public static final String ALIYUN_ACCESSKEY_SECRET = "VwwqFQFvTdkkSvsnSKCfgLe2gu7hab";

    /**
     * 阿里云短信API产品-地区（阿里云暂不支持多地区）
     */
    public static final String ALIYUN_SMS_REGION_HANGZHOU = "cn-hangzhou";

    /**
     * 阿里云短信API产品-有人模板code
     */
    public static final String ALIYUN_SMS_TEMPLATE = "SMS_171855330";

    /**
     * 阿里云短信API产品-有人短信签名
     */
    public static final String ALIYUN_SMS_SIGNNAME = "有人物联网";

    /**
     * 阿里云短信API产品-请求发送成功状态码
     */
    public static final String ALIYUN_SMS_STATE_SUCCESS = "OK";

    /**
     * 阿里云短信API产品-有人短信单个参数最大值
     */
    public static final int ALIYUN_SMS_PARAM_MAXLENGTH = 20;


    /**
     * 阿里云语音通知-主显号码
     */
    public static final String ALIYUN_VMS_SHOW_NUMBER = "06354545188";

    /**
     * 阿里云语音API产品-有人模板code
     */
    public static final String ALIYUN_VMS_TEMPLATE = "TTS_162738660";

    /**
     * 阿里云语音API产品-请求发送成功状态码
     */
    public static final String ALIYUN_VMS_STATE_SUCCESS = "OK";

}
