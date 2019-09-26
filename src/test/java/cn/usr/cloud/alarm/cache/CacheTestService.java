package cn.usr.cloud.alarm.cache;

import cn.usr.cloud.alarm.entity.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CacheTestService {


    @Cacheable(value= "person",key="#uid")
    public UserInfo getCache(String uid) {
        log.info("===getCache");
        UserInfo user = new UserInfo();
        user.setAccount("andy");
        user.setEmail("3722122@ww.ww");
        user.setAutoWorkorder(1);
        user.setId(11);
        user.setAutoWorkorder(233);
        user.setTel("18888888888");
        return user;
    }
}
