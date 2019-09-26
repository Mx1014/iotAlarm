package cn.usr.cloud.alarm.config;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: Andy(李永强)
 * @Date: 2019/6/18 上午10:05
 * @Describe: 云朵外部kafaka配置
 */
@Configuration
public class KafkaExternalConfig {

    /**
     * kafka地址
     */
    @Value("${spring.kafka.external.bootstrap-servers}")
    private String bootstrapServers;
    /**
     * 默认组
     */
    @Value("${spring.kafka.external.consumer.group-id}")
    private String groupId;
    /**
     * 自动消费设定
     */
    @Value("${spring.kafka.external.consumer.auto-offset-reset}")
    private String autoOffsetReset;

    /**
     * 自动提交
     */
    @Value("${spring.kafka.external.consumer.enable-auto-commit}")
    private String enableAutoCommit;

    /**
     * 自动ack设定
     */
    @Value("${spring.kafka.external.consumer.auto-commit-interval}")
    private String autoCommitInerval;


    @Value("${spring.kafka.external.producer.batch-size}")
    private int batchSize;


    @Value("${spring.kafka.external.producer.buffer-memory}")
    private int bufferMemory;

    @Bean("externalConsumerConfig")
    public Map<String, Object> consumerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitInerval);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        return props;
    }

    @Bean("externalProducerConfig")
    public Map<String, Object> producerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
        return props;
    }

/*    @Bean("externalKafkaListenerContainerFactory")
    ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(@Qualifier("insideConsumerFactory") ConsumerFactory consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setBatchListener(true);
        return factory;
    }*/

    @Bean(name = "externalKafkaListenerContainerFactory")
    @ConditionalOnMissingBean(name = "externalKafkaListenerContainerFactory")
    //重要！！！指定该ContainerFactory为主要的容器工厂，kafka消费者默认关联该容器
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory(@Qualifier("externalConsumerFactory") ConsumerFactory consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(3);
        factory.getContainerProperties().setPollTimeout(3000);
        return factory;
    }

    @Bean("externalConsumerFactory")
    public ConsumerFactory<String, String> consumerFactory(@Qualifier("externalConsumerConfig") Map<String, Object> consumerConfig) {
        Map<String, Object> properties = consumerConfig();
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        //return new DefaultKafkaConsumerFactory<String, String>(properties);
        return new DefaultKafkaConsumerFactory<>(properties);
    }

    @Bean("externalProducerFactory")
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean("externalKafkaTemplate")
    public KafkaTemplate<String, String> kafkaTemplate(@Qualifier("externalProducerFactory") ProducerFactory producerFactory) {
        KafkaTemplate<String,String> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        return kafkaTemplate;
    }
}
