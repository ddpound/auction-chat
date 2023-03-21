package com.example.auctionchat.service;

import com.example.auctionchat.config.SinkComponent;
import com.example.auctionchat.dto.AuctionRaiseDto;
import com.example.auctionchat.mongomodel.ProductModel;
import com.example.auctionchat.mongorepository.ProductRepository;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.changestream.OperationType;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.bson.Document;

import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.*;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;



@RequiredArgsConstructor
@Log4j2
@Service
public class ProductService {


    private final ProductRepository productRepository;

    private final SinkComponent sinkComponent;

    private final ReactiveMongoTemplate mongoTemplate;



    public Mono<ProductModel> saveProduct(ProductModel productModel){

        productModel.setCreateAt(LocalDateTime.now());
        return productRepository.save(productModel);
    }

    public Flux<ResponseEntity<ProductModel>> findListProduct(int roomNum){
        return productRepository.findRoomReturnProduct(roomNum)
                .map(productModel -> new ResponseEntity<ProductModel>(productModel, HttpStatus.OK))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Flux<ResponseEntity<ProductModel>> watchProductUpdates(int roomNum) {
        System.out.println("번호 1: " + roomNum);

        try{
            ChangeStreamOptions options = ChangeStreamOptions.builder()
                    .filter(Aggregation.newAggregation(ProductModel.class, Aggregation.match(
                            new Criteria().orOperator(
                                    Criteria.where("operationType").is(OperationType.UPDATE.getValue()),
                                    Criteria.where("operationType").is(OperationType.REPLACE.getValue()),
                                    Criteria.where("operationType").is(OperationType.INVALIDATE.getValue()),
                                    Criteria.where("operationType").is(OperationType.INSERT.getValue())))))
                    .returnFullDocumentOnUpdate().build();

            System.out.println("번호 2: " + options.getCollation());
            System.out.println("번호 3: " + options.getFilter());

            Query query = new Query();
            query.addCriteria(Criteria.where("roomNum").is(roomNum));

            Flux<ResponseEntity<ProductModel>> merge1 =  mongoTemplate.tail(query, ProductModel.class)
                    .map(productModel -> {
                        System.out.println(productModel);
                        return new ResponseEntity<>(productModel,HttpStatus.OK);
                    }).subscribeOn(Schedulers.boundedElastic());

            Flux<ResponseEntity<ProductModel>> merge2 =  mongoTemplate.changeStream("productModel",options,ProductModel.class)
                    .filter(productModelChangeStreamEvent -> productModelChangeStreamEvent.getBody().getRoomNum() == roomNum)
                    .map(productModelChangeStreamEvent
                            -> new ResponseEntity<>(productModelChangeStreamEvent.getBody(),HttpStatus.OK));

            return Flux.merge(merge1,merge2);

//            return mongoTemplate.changeStream(ProductModel.class)
//                    .watchCollection("productModel")
//                    .filter(Aggregation.newAggregation(ProductModel.class, Aggregation.match(
//                            new Criteria().orOperator(
//                                    Criteria.where("operationType").is(OperationType.UPDATE.getValue()),
//                                    Criteria.where("operationType").is(OperationType.REPLACE.getValue()),
//                                    Criteria.where("operationType").is(OperationType.INSERT.getValue())))))
//                    .listen()
//                    .map(productModelChangeStreamEvent -> new ResponseEntity<>(productModelChangeStreamEvent.getBody(),HttpStatus.OK))
//                    .subscribeOn(Schedulers.boundedElastic());
        }catch (Exception e){
            return null;
        }
    }




    public Mono<ResponseEntity<String>> raisePriceProduct(AuctionRaiseDto auctionRaiseDto){


//        return productRepository.findById(auctionRaiseDto.getProduct().getId())
//                .publishOn(Schedulers.single())
//                .flatMap((productModel)->{
//                    ProductModel newProductModel = ProductModel.builder()
//                            .auction(auctionRaiseDto.getProduct().isAuction())
//                            .roomNum(auctionRaiseDto.getProduct().getRoomNum())
//                            .finalBuyer(auctionRaiseDto.getUserdata().getNickName())
//                            .name(auctionRaiseDto.getProduct().getName())
//                            .createAt(auctionRaiseDto.getProduct().getCreateAt())
//                            .price(auctionRaiseDto.getProduct().getPrice()+auctionRaiseDto.getRaisePrice())
//                            .quantity(auctionRaiseDto.getProduct().getQuantity())
//                            .seller(auctionRaiseDto.getProduct().getSeller())
//                            .build();
//
//                    productRepository.save(newProductModel).subscribe();
//                    productRepository.delete(auctionRaiseDto.getProduct()).subscribe();
//
//                    return Mono.just(new ResponseEntity<String>("success raise price " , HttpStatus.OK));
//                });

        return productRepository.findById(auctionRaiseDto.getProduct().getId())
                .flatMap((productModel)->{
                    Query query = new Query();
                    query.addCriteria(Criteria.where("id").is(auctionRaiseDto.getProduct().getId()));

                    Update update = new Update();
                    update.set("finalBuyer", auctionRaiseDto.getUserdata().getNickName());
                    update.set("price", productModel.getPrice() + auctionRaiseDto.getRaisePrice());
                    update.set("createAt", LocalDateTime.now());

                    return mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true).upsert(true), ProductModel.class)
                            .subscribeOn(Schedulers.boundedElastic())
                            .map(productModel1 -> new ResponseEntity<String>("success raise price " + productModel1.getPrice(),HttpStatus.OK));
                }).doFinally(signalType -> {
                    System.out.println("스트림 변경 알림");
                    mongoTemplate.changeStream(ProductModel.class);
                });
    }

}
