package com.example.auctionchat.service;

import com.example.auctionchat.dto.AuctionRaiseDto;
import com.example.auctionchat.mongomodel.ChatModel;
import com.example.auctionchat.mongomodel.OrderModel;
import com.example.auctionchat.mongomodel.ProductModel;
import com.example.auctionchat.mongorepository.OrderRepository;
import com.example.auctionchat.mongorepository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Log4j2
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final ReactiveMongoTemplate mongoTemplate;
    public Mono<ResponseEntity<String>> saveOrder(OrderModel orderModel){

        if(orderModel.getQuantity() <= 0){
            return Mono.just(new ResponseEntity<>("fail order save, less than zero", HttpStatus.BAD_REQUEST));
        }

        try{
            return productRepository.findById(orderModel.getProductModel().getId())
                    .publishOn(Schedulers.boundedElastic())
                    .map(productModel -> {

                        if(productModel.getQuantity() <= 0
                                || (productModel.getQuantity()-orderModel.getQuantity()) <= 0){

                            return new ResponseEntity<>("sorry quantity is zero", HttpStatus.OK);
                        }else{

                            Query query = new Query();
                            query.addCriteria(Criteria.where("id").is(productModel.getId()));

                            Update update = new Update();
                            update.set("quantity", productModel.getQuantity()-orderModel.getQuantity());


                            mongoTemplate
                                    .findAndModify(query,update, FindAndModifyOptions.options().returnNew(true).upsert(true), ProductModel.class)
                                    .subscribe();

                            orderModel.setProductModel(productModel);
                            orderRepository.save(orderModel).subscribe();
                            return new ResponseEntity<>("success order save", HttpStatus.OK);
                        }
                    }).subscribeOn(Schedulers.single());

        }catch (Exception e){
            return Mono.just(new ResponseEntity<>("Sorry Error : " + Arrays.toString(e.getStackTrace()), HttpStatus.OK));
        }
    }

    public Mono<ResponseEntity<String>> deleteOrder(OrderModel orderModel){

        return orderRepository
                .delete(orderModel)
                .map(orderModel1 -> new ResponseEntity<>("success delete", HttpStatus.OK))
                .subscribeOn(Schedulers.single());
    }

    public Flux<ResponseEntity<OrderModel>> findMyOrder(int userId){


        return orderRepository.findMyOrder(userId)
                .map(orderModel -> {
                    System.out.println(orderModel.getProductModel());

                    return new ResponseEntity<OrderModel>(orderModel,HttpStatus.OK);
                })
                .subscribeOn(Schedulers.boundedElastic());
    }



    public Flux<OrderModel> checkOrderList(OrderModel orderModel){

        return orderRepository.findAll();
    }


}
