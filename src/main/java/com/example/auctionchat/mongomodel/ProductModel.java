package com.example.auctionchat.mongomodel;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

/**
 * 실시간 옥션에 올려놓을 물건을 말함
 * */
@Data
@Builder
@Document(collection = "productModel")
public class ProductModel {

    @Id
    private String id;

    private String name;

    private int price;

    private int quantity;
}
