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

    private int seller;

    private int roomNum;

    // 경매 유무를 따짐, 참일때 경매제품,
    // 아니면 일반제품
    private boolean auction;

    // 실시간 경매 유무를 따짐
    private boolean auctionState;

    private int buyerId;

    private String finalBuyer;

    private int quantity;
}
