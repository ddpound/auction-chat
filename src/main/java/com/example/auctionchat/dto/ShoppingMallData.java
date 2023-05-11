package com.example.auctionchat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * seller 어플리케이션이 구매내역을 요구했을 때 받는 DTO
 *
 * */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShoppingMallData {

    // 판매자 Id
    private int userId;

    private String shoppingMallName;

    private String shoppingMallExplanation;

}
