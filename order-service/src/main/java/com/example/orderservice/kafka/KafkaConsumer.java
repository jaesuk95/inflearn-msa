package com.example.orderservice.kafka;

import com.example.orderservice.model.OrderEntity;
import com.example.orderservice.model.OrdersRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {

    private final OrdersRepository ordersRepository;

    @KafkaListener(topics = "orders")
    public void processMessage(String kafkaMessage) {
        log.info("Kafka message: => {}", kafkaMessage);

        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        OrderEntity entity = ordersRepository.findByOrderId((String) map.get("productId"));
        if (entity != null) {

        }

    }
}
