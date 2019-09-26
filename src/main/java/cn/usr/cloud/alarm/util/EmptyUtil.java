package cn.usr.cloud.alarm.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * @Auther: Andy(李永强)
 * @Date: 2019/4/29 下午3:18
 * @Describe: 判空工具
 */
public class EmptyUtil {
    private EmptyUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 判断对象是否为空
     *
     * @param objs 对象
     * @return {@code true}: 为空<br>{@code false}: 不为空
     */
    public static boolean isEmpty(Object... objs) {
        for(Object obj : objs){
            if (obj == null) {
                return true;
            }
            if (obj instanceof String && obj.toString().length() == 0) {
                return true;
            }
            if (obj.getClass().isArray() && Array.getLength(obj) == 0) {
                return true;
            }
            if (obj instanceof Collection && ((Collection) obj).isEmpty()) {
                return true;
            }
            if (obj instanceof Map && ((Map) obj).isEmpty()) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断对象是否非空
     *
     * @param objs 对象
     * @return {@code true}: 非空<br>{@code false}: 空
     */
    public static boolean isNotEmpty(Object... objs) {
        return !isEmpty(objs);
    }
}
