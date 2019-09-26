package cn.usr.cloud.alarm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(
        scanBasePackages={"cn.usr.cloud.alarm"}
		,exclude = {
        	DataSourceAutoConfiguration.class,
			KafkaAutoConfiguration.class
        }
)
//@MapperScan("cn.usr.cloud.alarm.mapper")
@EnableCaching  //开启缓存
public class AlarmApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlarmApplication.class, args);
	}
}
