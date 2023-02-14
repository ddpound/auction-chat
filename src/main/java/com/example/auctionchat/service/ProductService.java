package com.example.auctionchat.service;

import com.example.auctionchat.mongomodel.ProductModel;
import com.example.auctionchat.mongorepository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Log4j2
@Service
public class ProductService {


    private final ProductRepository productRepository;


    public Mono<ProductModel> saveProduct(ProductModel productModel){
        return productRepository.save(productModel);
    }

}
