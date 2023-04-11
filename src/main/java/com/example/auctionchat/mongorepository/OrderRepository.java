package com.example.auctionchat.mongorepository;

import com.example.auctionchat.mongomodel.OrderModel;
import com.example.auctionchat.mongomodel.Room;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface OrderRepository extends ReactiveMongoRepository<OrderModel,String> {


    @Query("{buyer: ?0 , seller:  ?1}")
    Flux<OrderModel> findMyOrderWithSeller(int buyer, int seller);

    @Query("{buyer: ?0}")
    Flux<OrderModel> findMyOrder(int buyer);

    

}
