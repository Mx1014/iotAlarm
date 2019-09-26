package cn.usr.cloud.alarm.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class DBTests {

    @Autowired
    //AliyunIotAccountMapper aliyunIotAccountMapper;

	@Test
	public void contextLoads() {
        //AliyunIotAccountEntity entity = aliyunIotAccountMapper.getOne();
        //System.out.println(entity.toString());
        //log.info(entity.toString());
	}

}
