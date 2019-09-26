package cn.usr.cloud.alarm.redis;

import cn.usr.cloud.alarm.util.ProtoStuffSerializerUtil;
import cn.usr.cloud.alarm.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.dto.Point;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

import static cn.usr.cloud.alarm.constant.ConstantCacheKey.CACHE_ALARM_RECORD;

/**
 * @Auther: Andy(李永强)
 * @Date: 2019/4/28 上午11:42
 * @Describe:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RedisTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void testRedis(){
        long start1 = System.currentTimeMillis();
        for(int i=1;i<1;i++){
//            AliyunIotAccountEntity aliyunIotAccountEntity = new AliyunIotAccountEntity();
//            aliyunIotAccountEntity.setAccessKey("1111");
//            aliyunIotAccountEntity.setId(1+i);
//            aliyunIotAccountEntity.setSystemId(2+i);
//            redisTemplate.opsForHash().put("t1","1"+i,aliyunIotAccountEntity);
           // AliyunIotAccountEntity aliyunIotAccountEntity1 = (AliyunIotAccountEntity)redisTemplate.opsForHash().get("t1","1"+i);
            //log.info(aliyunIotAccountEntity1.toString());
        }
        long end1 = System.currentTimeMillis();
        log.info(""+(end1 - start1));


        start1 = System.currentTimeMillis();
        for(int i=0;i<1000;i++){
//            AliyunIotAccountEntity aliyunIotAccountEntity = new AliyunIotAccountEntity();
//            aliyunIotAccountEntity.setAccessKey("2222");
//            aliyunIotAccountEntity.setId(1+i);
//            aliyunIotAccountEntity.setSystemId(2+i);
//            //byte[] a = ProtoStuffSerializerUtil.serialize(aliyunIotAccountEntity);
//            redisTemplate.opsForHash().put("t2","1"+i,aliyunIotAccountEntity);
//            AliyunIotAccountEntity aliyunIotAccountEntity1 = (AliyunIotAccountEntity)redisTemplate.opsForHash().get("t2","1"+i);
//            //ProtoStuffSerializerUtil.deserialize(aliyunIotAccountEntity1,AliyunIotAccountEntity.class);
//            log.info(aliyunIotAccountEntity1.toString());
        }
        end1 = System.currentTimeMillis();
        log.info(""+(end1 - start1));
    }

    @Test
    public void testRedis1(){
        //for(int i =0; i < 100; i++){
            Point.Builder builder = Point.measurement("alarm");
            builder.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                    .tag("TriggerId", "1")
                    .tag("ItemId", "2")
                    .tag("SlaveIndex", "3")
                    .tag("DeviceId", "4");

            builder.addField("Content", "Content")
                    .addField("UserId", 1231231)
                    .addField("AlarmState",  1)
                    .addField("Value", "111222");
            redisTemplate.opsForList().rightPush(CACHE_ALARM_RECORD, builder.build());
            //redisUtil.rPush(CACHE_ALARM_RECORD, builder.build());
        //}



    }
}
