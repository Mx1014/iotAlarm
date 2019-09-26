package cn.usr.cloud.alarm.config;

import cn.usr.cloud.alarm.util.ProtostuffRedisSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * @author 李永强
 * @date 2019年04月28日15:37:17
 */
@Configuration
public class RedisConfig {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置redisTemplate的序列化插件
     * @return
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(){
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);

        ProtostuffRedisSerializer objectSerializer = new ProtostuffRedisSerializer();
        redisTemplate.setValueSerializer(objectSerializer);
        redisTemplate.setHashValueSerializer(objectSerializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}