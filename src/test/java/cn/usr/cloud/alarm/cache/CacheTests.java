package cn.usr.cloud.alarm.cache;

import cn.usr.cloud.alarm.dto.AlarmContactsDTO;
import cn.usr.cloud.alarm.entity.UserInfo;
import cn.usr.cloud.alarm.service.AlarmConfigService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@CacheConfig(cacheNames = "user")
public class CacheTests {

    @Autowired
    AlarmConfigService alarmConfigService;

    @Autowired
    CacheTestService cacheTestService;


	@Test
    public void testCache() {
        //redisTemplate.opsForValue().set("111","222");
//        List<Trigger> list = alarmConfigService.getTriggerByDataPointId(35305);
        //List<AlarmContactsDTO> list = alarmConfigService.getContactListByTriggerId(1161);
        cacheTestService.getCache("111");
        UserInfo user = cacheTestService.getCache("111");
        log.info(user.toString());
        //log.info(list.toString());
    }

}
