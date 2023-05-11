package com.example.catalogservice.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    // topic 에 들어가기 위한 접속하기 위한 consumer factory 등록
    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        HashMap<String, Object> properties = new HashMap<>();
        // kafka container host
//        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.01:9092");   // 주소 입력
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.18.0.101:9092");   // 주소 입력 172.18.0.101
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "consumerGroupId");          //
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);     // topic 에 담겨있는 데이터는 json 형식, key,value 형식으로 저장되어 있어 deserializer 적용
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(properties);
    }

    // topic 에 변경 사항이 있는지, 계속 listening 하고 있는, event 가 발생했을 때 그것을 catch 할 수 있는 listener 등록
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String>
                kafkaListenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        kafkaListenerContainerFactory.setConsumerFactory(consumerFactory());
        return kafkaListenerContainerFactory;
    }
}
