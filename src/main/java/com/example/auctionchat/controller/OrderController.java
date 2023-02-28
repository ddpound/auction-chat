package com.example.auctionchat.controller;

import com.example.auctionchat.models.Order;
import com.example.auctionchat.mongomodel.OrderModel;
import com.example.auctionchat.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Log4j2
@RequestMapping(value = "user/order")
@RestController
public class OrderController {

    private final OrderService orderService;


    @PostMapping(value = "save")
    public Mono<ResponseEntity<String>> saveOrder(@RequestBody OrderModel orderModel){

        return orderService.saveOrder(orderModel);
    }



    @DeleteMapping(value = "delete")
    public Mono<ResponseEntity<String>> deleteOrder(@RequestBody OrderModel orderModel){

        return orderService.deleteOrder(orderModel);
    }


}
