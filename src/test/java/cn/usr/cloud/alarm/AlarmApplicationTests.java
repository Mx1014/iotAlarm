package cn.usr.cloud.alarm;

import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AlarmApplicationTests {

	@Autowired
	private InfluxDB influxDB;
	@Test
	public void contextLoads() {
		String dbName = "mydb";


        long start = System.currentTimeMillis();
        for(int i=0; i < 1; i++){
            BatchPoints batchPoints = BatchPoints.database("alarm")
                    .consistency(InfluxDB.ConsistencyLevel.ALL).build();
            for(int n =0; n < 5; n ++){
                batchPoints.point(Point.measurement("cpu3")
                        .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                        .addField("idle", i)
                        .addField("user", i)
                        .addField("system", i)
                        .build());
            }
            influxDB.write(batchPoints);
        }

        long end = System.currentTimeMillis();
		long time = end-start;
        log.info("消耗时间：{}毫秒",time);
        time = time/1000;
        log.info("消耗时间：{}秒",time);
        float count = 10000 / time;
        log.info("消耗时间：{}毫秒，速率：{}/s",time,count);
		influxDB.close();
	}

}
