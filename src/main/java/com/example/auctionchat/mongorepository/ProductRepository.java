package com.example.auctionchat.mongorepository;

import com.example.auctionchat.mongomodel.ProductModel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductRepository extends ReactiveMongoRepository<ProductModel,String> {
}
