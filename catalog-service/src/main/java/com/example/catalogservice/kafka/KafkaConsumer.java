package com.example.catalogservice.kafka;

import com.example.catalogservice.model.CatalogueEntity;
import com.example.catalogservice.model.CatalogueRepository;
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

    private final CatalogueRepository catalogueRepository;

    @KafkaListener(topics = "example-order-topic")
    public void processMessage(String kafkaMessage) {
        log.info("Kafka message: => {}", kafkaMessage);

        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        CatalogueEntity entity = catalogueRepository.findByProductId((String) map.get("productId"));
        entity.setStock(entity.getStock() - (Integer)map.get("qty"));

        catalogueRepository.save(entity);
    }
}
