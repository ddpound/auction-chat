package com.example.auctionchat.controller;

import com.example.auctionchat.dto.AuctionRaiseDto;

import com.example.auctionchat.mongomodel.OrderModel;
import com.example.auctionchat.mongomodel.ProductModel;
import com.example.auctionchat.service.OrderService;
import com.example.auctionchat.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RequiredArgsConstructor
@Log4j2
@RequestMapping(value = "user/order")
@RestController
public class OrderController {

    private final OrderService orderService;

    private final ProductService productService;

    @PostMapping(value = "save")
    public Mono<ResponseEntity<String>> saveOrder(@RequestBody OrderModel orderModel){


        return orderService.saveOrder(orderModel);
    }

    @DeleteMapping(value = "delete")
    public Mono<ResponseEntity<String>> deleteOrder(@RequestBody OrderModel orderModel){

        return orderService.deleteOrder(orderModel);
    }

    @PostMapping(value = "raise")
    public Mono<ResponseEntity<String>> raisePorudct(@RequestBody AuctionRaiseDto auctionRaiseDto){

        System.out.println(auctionRaiseDto);
        return productService
                .raisePriceProduct(auctionRaiseDto)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping(value = "find-my-order/{userId}")
    public Flux<ResponseEntity<OrderModel>> findMyOrder(@PathVariable int userId){

        return orderService.findMyOrder(userId);
    }


}
