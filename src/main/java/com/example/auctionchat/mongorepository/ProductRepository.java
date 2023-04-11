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


    @Query("{finalBuyer :  ?0, roomNum :  ?1}")
    Flux<ProductModel> findAuctionFinalBuyer(String finalByuer, int roomNum);

}
