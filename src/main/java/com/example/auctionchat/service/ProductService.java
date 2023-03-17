package com.example.auctionchat.service;

import com.example.auctionchat.config.SinkComponent;
import com.example.auctionchat.dto.AuctionRaiseDto;
import com.example.auctionchat.mongomodel.ProductModel;
import com.example.auctionchat.mongorepository.ProductRepository;

import com.mongodb.client.model.Filters;
import com.mongodb.reactivestreams.client.MongoClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.bson.Document;

import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.*;
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
import java.util.Collections;
import java.util.function.Predicate;

import static org.springframework.data.mongodb.core.query.Criteria.where;


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

    public Flux<ProductModel> findListProduct(int roomNum){
        return productRepository.findRoomReturnProduct(roomNum)
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Flux<ResponseEntity<ProductModel>> watchProductUpdates(int roomNum) {
        ChangeStreamOptions options = ChangeStreamOptions.builder()
                .filter(Document.parse("{ $match: { operationType: { $in: [ 'insert', 'update' ] } } }"))
                .build();

        return mongoTemplate.changeStream("productModel", options, ProductModel.class)
                .filter(productModelChangeStreamEvent -> productModelChangeStreamEvent.getBody().getRoomNum() == roomNum)
                .map(change -> new ResponseEntity<>(change.getBody(), HttpStatus.OK))
                .subscribeOn(Schedulers.boundedElastic());
    }




    public Mono<ProductModel> raisePriceProduct(AuctionRaiseDto auctionRaiseDto){

        return productRepository.findById(auctionRaiseDto.getProduct().getId())
                .flatMap((productModel)->{
                    Query query = new Query();
                    query.addCriteria(where("id").is(auctionRaiseDto.getProduct().getId()));

                    Update update = new Update();
                    update.set("finalBuyer", auctionRaiseDto.getUserdata().getNickName());
                    update.set("price", productModel.getPrice() + auctionRaiseDto.getRaisePrice());
                    update.set("createAt", LocalDateTime.now());

                    return mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true).upsert(true), ProductModel.class)
                            .subscribeOn(Schedulers.boundedElastic());
                }).doFinally(signalType -> {
                    System.out.println("스트림 변경 알림");
                    mongoTemplate.changeStream(ProductModel.class);
                });
    }

}
