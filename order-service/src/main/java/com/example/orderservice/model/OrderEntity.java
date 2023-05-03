package com.example.orderservice.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity
@Table(name = "ORDERS")
// Serializable 사용 이유: 가지고 있는 Object 객체를 전송하거나 다른 network 전송 또는 데이터 베이스에 보관하기 위해서 XML로 변환하는 마샬링(Marshalling) unmarshalling 하기 위해서 사용하는게 직렬화이다.
public class OrderEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String productId;
    @Column(nullable = false)
    private Integer qty;
    @Column(nullable = false)
    private Integer unitPrice;
    @Column(nullable = false)
    private Integer totalPrice;

    @Column(nullable = false)
    private String userId;
    @Column(nullable = false, unique = true)
    private String orderId;

//    @Column(nullable = false, updatable = false, insertable = false)
//    @ColumnDefault(value = "CURRENT_TIMESTAMP")
//    private Date date;

    @Column(name = "timestamp", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    public Timestamp date;
}
