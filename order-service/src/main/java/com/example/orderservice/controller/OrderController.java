package com.example.orderservice.controller;

import com.example.orderservice.controller.request.RequestOrder;
import com.example.orderservice.controller.response.ResponseOrder;
import com.example.orderservice.kafka.KafkaProducer;
import com.example.orderservice.kafka.producer.KafkaOrderProducer;
import com.example.orderservice.model.OrderDto;
import com.example.orderservice.model.OrderEntity;
import com.example.orderservice.model.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/order-service")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final Environment env;
    private final KafkaProducer kafkaProducer;
    private final KafkaOrderProducer kafkaOrderProducer;

    @GetMapping("/welcome")
    public String welcome() {
        return String.format("welcome to catalogue service %s", env.getProperty("local.server.port"));
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId")String userId, @RequestBody RequestOrder orderDetails) {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        OrderDto orderDto = modelMapper.map(orderDetails, OrderDto.class);
        orderDto.setUserId(userId);

//        jpa 방식
//        OrderDto createDto = orderService.createOrder(orderDto);
//        ResponseOrder returnValue = modelMapper.map(createDto, ResponseOrder.class);

        // kafka
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(orderDetails.getQty() * orderDetails.getUnitPrice());
        ResponseOrder returnValue = modelMapper.map(orderDto, ResponseOrder.class);

        // send an order to kafka
        kafkaProducer.send("example-order-topic", orderDto);
        kafkaOrderProducer.send("orders", orderDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrder(@PathVariable("userId") String userId) throws Exception {
        log.info("Before retrieve orders data");
        Iterable<OrderEntity> orderList = orderService.getOrdersByUserId(userId);

        List<ResponseOrder> result = new ArrayList<>();
        orderList.forEach(v -> {
            result.add(new ModelMapper().map(v, ResponseOrder.class));
        });

//        try {
//            Thread.sleep(3000);
//            throw new Exception("TESTING 장애 발생");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        log.info("After retrieved orders Data");

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
