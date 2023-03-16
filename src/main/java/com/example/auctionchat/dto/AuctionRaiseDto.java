package com.example.auctionchat.dto;

import com.example.auctionchat.mongomodel.ProductModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 실시간 경매시 받을 DTO
 * 제품, 입찰가격, 최고 입찰자,
 * */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AuctionRaiseDto {

    private ProductModel product;

    private int raisePrice;

    private UserData userdata;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public class UserData{
        private int id;
        private String nickName;
        private String picture;
        private String role;
        private String userName;

    }

}


