package com.example.auctionchat.service;

import com.example.auctionchat.mongomodel.OrderModel;
import com.example.auctionchat.mongorepository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@RequiredArgsConstructor
@Log4j2
@Service
public class OrderService {

    private final OrderRepository orderRepository;


    public Mono<ResponseEntity<String>> saveOrder(OrderModel orderModel){

        return orderRepository
                .save(orderModel)
                .map(orderModel1 -> new ResponseEntity<>("success save", HttpStatus.OK))
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
