package com.example.orderservice.kafka;

import com.example.orderservice.kafka.dto.Payload;
import com.example.orderservice.model.OrderEntity;
import com.example.orderservice.model.OrdersRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
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

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Payload payload = modelMapper.map(map.get("payload"), Payload.class);

        OrderEntity orderEntity = new OrderEntity(
                payload.getProduct_id(),
                payload.getQty(),
                payload.getUnit_price(),
                payload.getTotal_price(),
                payload.getUser_id(),
                payload.getOrder_id()
        );

        ordersRepository.save(orderEntity);
        log.info("@KafkaListener -> new data has been saved under orderEntity");
    }
}
