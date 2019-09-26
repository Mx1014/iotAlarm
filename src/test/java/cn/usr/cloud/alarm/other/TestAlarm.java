package cn.usr.cloud.alarm.other;

import cn.usr.cloud.alarm.entity.DeviceStatus;
import cn.usr.cloud.alarm.entity.Trigger;
import cn.usr.cloud.alarm.ignite.DeviceStatusMapper;
import cn.usr.cloud.alarm.util.EmptyUtil;
import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.StringTokenizer;

import static cn.usr.cloud.alarm.constant.ConstanTriggerCondition.*;

/**
 * @Auther: Andy(李永强)
 * @Date: 2019/6/5 下午12:08
 * @Describe:
 */
@Slf4j
//@RunWith(SpringRunner.class)
@SpringBootTest
public class TestAlarm {

    @Test
    public void alarmPDTest(){
        boolean alarmStatus = false;
        Trigger trigger = new Trigger();
        trigger.setInputValue(0.9);
        trigger.setCondition(LESS);
        trigger.setMin(2.0);
        trigger.setMax(5.0);
        trigger.setDeadZone(1.0);
        boolean state = trigger.isSufficientDeadZoneValue(alarmStatus);
        log.info("变化：{}",state ? "true":"false");
        log.info("由 {} 转为 {}",alarmStatus ? "报警":"正常", state ? "报警":"正常");

    }

    @Test
    public void subStringTest(){
        String a = "hello".substring(0,38);
        log.info(a);

    }

    @Test
    public void jsonArrayforEachTest(){
        JSONArray array = new JSONArray();
        array.add("11");
        array.add(22);
        array.forEach(item ->
            log.info(item.toString())
        );
    }

    @Test
    public void test1(){
        String aa = "aa:bb:cc";
        StringTokenizer tokenizer = new StringTokenizer(aa,":");
        tokenizer.nextElement();
        String deviceId = (String)tokenizer.nextElement();
        System.out.println(deviceId);
    }

    @Test
    public void test2(){
        Integer a = 1;
        if(a.equals(1)){
            log.info("true");
        }
    }

    @Autowired
    DeviceStatusMapper deviceStatusMapper;
    @Test
    public void igniteTest(){
        DeviceStatus deviceStatus1 = new DeviceStatus("bbcc", 1);
        DeviceStatus deviceStatus = deviceStatusMapper.getDeviceStatus("bbcc");
        if(EmptyUtil.isEmpty(deviceStatus)){
            deviceStatusMapper.insertDeviceStatus(deviceStatus1);
        }else{
            deviceStatusMapper.updateDeviceStatus(deviceStatus1);
        }
        //log.info(deviceStatus.toString());
    }
}
