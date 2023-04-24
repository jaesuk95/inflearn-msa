package com.example.orderservice.controller.request;

import lombok.Data;

import java.util.Date;

@Data
public class RequestOrder {
    private String productId;
    private Integer qty;
    private Integer unitPrice;
//    private Integer totalPrice;
//    private Date createdAt;
//    private String orderId;
}
