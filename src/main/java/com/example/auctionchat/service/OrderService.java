package com.example.auctionchat.service;

import com.example.auctionchat.dto.AuctionRaiseDto;
import com.example.auctionchat.mongomodel.ChatModel;
import com.example.auctionchat.mongomodel.OrderModel;
import com.example.auctionchat.mongomodel.ProductModel;
import com.example.auctionchat.mongorepository.OrderRepository;
import com.example.auctionchat.mongorepository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@RequiredArgsConstructor
@Log4j2
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;



    public Mono<ResponseEntity<String>> saveOrder(OrderModel orderModel){

        if(orderModel.getQuantity() <= 0){
            return Mono.just(new ResponseEntity<>("fail order save, less than zero", HttpStatus.BAD_REQUEST));
        }

        return orderRepository
                .save(orderModel)
                .map(orderModel1 -> new ResponseEntity<>("success order save", HttpStatus.OK))
                .subscribeOn(Schedulers.single());
    }

    public Mono<ResponseEntity<String>> deleteOrder(OrderModel orderModel){

        return orderRepository
                .delete(orderModel)
                .map(orderModel1 -> new ResponseEntity<>("success delete", HttpStatus.OK))
                .subscribeOn(Schedulers.single());
    }



    public Flux<OrderModel> checkOrderList(OrderModel orderModel){

        return orderRepository.findAll();
    }


}
