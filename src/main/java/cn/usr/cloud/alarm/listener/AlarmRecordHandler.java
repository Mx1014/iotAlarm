package cn.usr.cloud.alarm.listener;

import cn.usr.cloud.alarm.util.EmptyUtil;
import cn.usr.cloud.alarm.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static cn.usr.cloud.alarm.constant.ConstantCacheKey.CACHE_ALARM_RECORD;
import static cn.usr.cloud.alarm.constant.ConstantCacheKey.CACHE_LAST_SAVE_TIME;


/**
 * @Auther: Andy(李永强)
 * @Date: 2019/6/12 下午12:03
 * @Describe:
 */
@Component
@EnableScheduling   // 1.开启定时任务
@Slf4j
public class AlarmRecordHandler {

    @Value("${alarm.batch-save-size : 100}")
    private Integer batchSaveSize;

    @Value("${alarm.batch-retention-time : 5000}")
    private Integer maxRetentionTime;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    private InfluxDB influxDB;

    @Value("${spring.influx.database}")
    private String databaseName;

    public static boolean runing = false;

    @PostConstruct
    void starting(){
        log.info("AlarmRecordHandler开始启动");
        runing = true;

    }

    @PreDestroy
    void stoping(){
        log.info("AlarmRecordHandler停止");
        runing = false;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(fixedDelay = 2000)  //间隔2秒
    public void saveRecord() {
        if(!runing) return;
        ListOperations listOperations = redisTemplate.opsForList();
        ValueOperations valueOperations = redisTemplate.opsForValue();

        Long listSize = listOperations.size(CACHE_ALARM_RECORD);

        boolean saveFlag;
        while (saveFlag = listSize > batchSaveSize && runing){
            log.info("保存报警历史记录");
            BatchPoints batchPoints = BatchPoints.database(databaseName)
                    .consistency(InfluxDB.ConsistencyLevel.ALL).build();

            for(int i = 0; i < batchSaveSize; i++){
                if(!runing) break;
                Point point = null;
                try{
                    point = (Point) listOperations.leftPop(CACHE_ALARM_RECORD);
                }catch (Exception e){
                    e.printStackTrace();
                }

                if(EmptyUtil.isEmpty(point)) continue;
                batchPoints.point(point);
            }
            influxDB.write(batchPoints);
            if(!runing) return;
            listSize = listOperations.size(CACHE_ALARM_RECORD);
            //记录本次的保存记录的时间
            valueOperations.set(CACHE_LAST_SAVE_TIME, System.currentTimeMillis());
        }
        if(saveFlag || !runing) return;

        //下面的逻辑是，如果报警记录一直不超过batchSaveSize条数据，那么超过*秒之后强制保存
        Long lastSaveTime = (Long) valueOperations.get(CACHE_LAST_SAVE_TIME);
        //如果为空就初始化一下
        if(EmptyUtil.isEmpty(lastSaveTime)){
            valueOperations.set(CACHE_LAST_SAVE_TIME, System.currentTimeMillis());
            return;
        }

        long retentionTime = System.currentTimeMillis() - lastSaveTime;
        if(retentionTime < maxRetentionTime)
            return;

        //开始强制保存
        //log.info("强制保存报警历史记录");
        listSize = listOperations.size(CACHE_ALARM_RECORD);

        BatchPoints batchPoints = BatchPoints.database(databaseName)
                .consistency(InfluxDB.ConsistencyLevel.ALL).build();

        for(int i = 0; i < listSize; i++){
            //Point point = redisUtil.lPop(CACHE_ALARM_RECORD, Point.class);
            if(!runing) break;
            Point point = (Point) listOperations.leftPop(CACHE_ALARM_RECORD);
            batchPoints.point(point);
        }
        influxDB.write(batchPoints);
        valueOperations.set(CACHE_LAST_SAVE_TIME, System.currentTimeMillis());
    }

    /*@Scheduled(fixedDelay = 1000)  //间隔1秒
    public void saveRecord1() {
        for(int i =0; i < 100; i++){
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
            if(!runing) return;
            redisTemplate.opsForList().rightPush(CACHE_ALARM_RECORD, builder.build());
            //redisUtil.rPush(CACHE_ALARM_RECORD, builder.build());
        }
    }*/
}
