package com.example.auctionchat.service;

import com.example.auctionchat.mongomodel.OrderModel;
import com.example.auctionchat.mongorepository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Log4j2
@Service
public class OrderService {

    private final OrderRepository orderRepository;


    public Mono<OrderModel> saveOrder(OrderModel orderModel){

        return orderRepository.save(orderModel);
    }


    public Flux<OrderModel> checkOrderList(OrderModel orderModel){

        return orderRepository.findAll();
    }


}
