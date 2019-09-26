package cn.usr.cloud.alarm.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Auther: Andy(李永强)
 * @Date: 2019/4/28 下午3:43
 * @Describe:
 */
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * @param hashName
     * @param key
     * @param value
     */
    public void putHash(String hashName, String key, Object value){
        byte[] a = ProtoStuffSerializerUtil.serialize(value);
        redisTemplate.execute((connection) -> {
            connection.hSet(hashName.getBytes(), key.getBytes(), a);
            return null;
        }, true);
    }

    public <T> T getHash(String hashName, String key, Class<T> t){
        byte[] value = (byte[])redisTemplate.execute((connection) -> {
            return connection.hGet(hashName.getBytes(), key.getBytes());
        }, true);
        return value != null ? ProtoStuffSerializerUtil.deserialize(value, t) : null;
    }

    /**
     * @param key
     * @param value
     */
    public void rPush(String key, Object value){
        byte[] a = ProtoStuffSerializerUtil.serialize(value);
        redisTemplate.execute((connection) -> {
            connection.rPush(key.getBytes(), a);
            return null;
        }, true);
    }

    public <T> T lPop(String key, Class<T> t){
        byte[] value = (byte[])redisTemplate.execute((connection) -> {
            return connection.lPop(key.getBytes());
        }, true);
        return value != null ? ProtoStuffSerializerUtil.deserialize(value, t) : null;
    }

    public <T> T lPop(String key, Class<T> t, long timeout, TimeUnit unit){
        int tm = (int) TimeoutUtils.toSeconds(timeout, unit);
        byte[] value = (byte[])redisTemplate.execute((connection) -> {
            return connection.bLPop(tm, key.getBytes());
        }, true);
        return value != null ? ProtoStuffSerializerUtil.deserialize(value, t) : null;
    }

    /**
     * 删除hash中指定的key
     * @param hashName
     * @param keys
     * @return
     */
    public Long deleteHashKeys(Object hashName, Object... keys){
        return redisTemplate.opsForHash().delete(hashName, keys);
    }

}
