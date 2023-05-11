package com.example.auctionchat.mongorepository;

import com.example.auctionchat.mongomodel.ProductModel;
import org.springframework.data.mongodb.core.ChangeStreamEvent;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;

public interface ProductRepository extends ReactiveMongoRepository<ProductModel,String> {

    @Tailable
    @Query("{roomNum :  ?0}")
    Flux<ProductModel> findRoomReturnProduct(int roomNum);


    // 실시간으로 내 방에서의 경매 결과 보기
    // google userName으로 검색해서 중복되지 않음
    @Query("{finalBuyer :  ?0, roomNum :  ?1}")
    Flux<ProductModel> findAuctionFinalBuyer(String finalByuer, int roomNum);

    // 경매에서 구매한 구매내역 보기
    // orderList가 아니라 최종 구매하기로한 제품이기때문에
    @Query("{finalBuyer :  ?0, buyerId :  ?1 , seller:  ?2}")
    Flux<ProductModel> findAuctionPurchaseDetails(String finalByuer, int buyerId, int seller);

}
