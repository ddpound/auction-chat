package com.example.auctionchat.mongomodel;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

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

    @DocumentReference
    private ProductModel productModel;

    private String orderName;

    private int seller;

    private int buyer;

    private int price;

    private int quantity;


}
