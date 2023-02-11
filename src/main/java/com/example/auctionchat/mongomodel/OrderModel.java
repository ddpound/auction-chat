package com.example.auctionchat.mongomodel;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;


/**
 * 주문 내역
 * */
@Data
@Builder
@Document(collection = "orderModel")
public class OrderModel {

    @Id
    private String id;

    private String productName;

    private String orderName;

    private int price;

    private int quantity;


}
