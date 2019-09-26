package cn.usr.cloud.alarm.config;

import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: Andy(李永强)
 * @Date: 2019/5/22 下午5:29
 * @Describe:
 */
@Slf4j
@Configuration
public class InfluxDbConfig {
    @Value("${spring.influx.url}")
    private String url;

    @Value("${spring.influx.user}")
    private String user;

    @Value("${spring.influx.password}")
    private String password;

    @Value("${spring.influx.database}")
    private String dataBase;

    @Bean
    public InfluxDB getConnect(){
        InfluxDB influxDB = InfluxDBFactory.connect(url, user, password);
//        InfluxDB influxDB = InfluxDBFactory.connect(url);
        influxDB.setDatabase(dataBase);
        return influxDB;
    }

}
