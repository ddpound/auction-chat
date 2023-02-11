package com.example.auctionchat.mongorepository;

import com.example.auctionchat.mongomodel.OrderModel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface OrderRepository extends ReactiveMongoRepository<OrderModel,String> {
}
