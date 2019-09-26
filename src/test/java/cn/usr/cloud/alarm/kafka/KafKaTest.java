package cn.usr.cloud.alarm.kafka;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Auther: Andy(李永强)
 * @Date: 2019/5/31 上午11:22
 * @Describe:
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class KafKaTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Test
    public void sendMessage(){

        JsonObject dataPoint = new JsonObject()
                .put("dataPointId", 35305)
                .put("value", 0)
                .put("timeStamp", 1560938027);

        JsonArray dataPoints = new JsonArray()
                .add(dataPoint);

        JsonObject message = new JsonObject()
                .put("deviceId","ddd")
                .put("dataPoints",dataPoints);
        kafkaTemplate.send("alarm",message.toString());
    }

    @KafkaListener(topics = {"test"}, containerFactory = "insideKafkaListenerContainerFactory")
    public void receiveMessage(String message){
        log.info(message);
    }

}
