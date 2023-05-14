package com.example.auctionchat.service;

import com.example.auctionchat.config.SinkComponent;
import com.example.auctionchat.dto.AuctionRaiseDto;
import com.example.auctionchat.mongomodel.ProductModel;
import com.example.auctionchat.mongorepository.ProductRepository;


import com.mongodb.client.model.changestream.OperationType;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


import org.joda.time.LocalDateTime;
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




@RequiredArgsConstructor
@Log4j2
@Service
public class ProductService {


    private final ProductRepository productRepository;

    private final SinkComponent sinkComponent;

    private final ReactiveMongoTemplate mongoTemplate;

    public Mono<ProductModel> saveProduct(ProductModel productModel){

        if(productModel.getPrice() < 0 ){
            return Mono.empty();
        }

        // 경매 시작일 때
        if(productModel.isAuction()){
            productModel.setAuctionState(true);
            productModel.setFinalBuyer("없음");
        }

        //productModel.setCreateAt(LocalDateTime.now());
        return productRepository.save(productModel)
                .doFinally(signalType -> {
                    log.info("success save product : " + productModel.getId()
                            + ", product name " + productModel.getName());
                });
    }

    public Flux<ResponseEntity<ProductModel>> findListProduct(int roomNum){
        return productRepository.findRoomReturnProduct(roomNum)
                .map(productModel -> new ResponseEntity<ProductModel>(productModel, HttpStatus.OK))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Flux<ResponseEntity<ProductModel>> watchProductUpdates(int roomNum) {
        try{
            ChangeStreamOptions options = ChangeStreamOptions.builder()
                    .filter(Aggregation.newAggregation(ProductModel.class, Aggregation.match(
                            new Criteria().orOperator(
                                    Criteria.where("operationType").is(OperationType.UPDATE.getValue()),
                                    Criteria.where("operationType").is(OperationType.REPLACE.getValue()),
                                    Criteria.where("operationType").is(OperationType.INVALIDATE.getValue()),
                                    Criteria.where("operationType").is(OperationType.INSERT.getValue())))))
                    .returnFullDocumentOnUpdate().build();

            Query query = new Query();
            query.addCriteria(Criteria.where("roomNum").is(roomNum));

            // 하나는 insert만 감지, 다른하나는 update만 감지해 둘을 merge시킨다.
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
        }catch (Exception e){
            return null;
        }
    }



    /**
     *
     * */
    public Mono<ResponseEntity<String>> raisePriceProduct(AuctionRaiseDto auctionRaiseDto){

        log.info("auction Raise working");
        if(auctionRaiseDto.getRaisePrice() <= 0 ){
            log.info("raisePrice fail because price not over 0 won");
            return Mono.just(new ResponseEntity<String>("I'm sorry. The bidding price is over 0 won", HttpStatus.BAD_REQUEST));
        }else {
            return productRepository.findById(auctionRaiseDto.getProduct().getId())
                    .flatMap((productModel)->{
                        Query query = new Query();
                        query.addCriteria(Criteria.where("id").is(auctionRaiseDto.getProduct().getId()));

                        Update update = new Update();
                        update.set("finalBuyer", auctionRaiseDto.getUserdata().getNickName());
                        update.set("price", productModel.getPrice() + auctionRaiseDto.getRaisePrice());
                        update.set("buyerId", auctionRaiseDto.getUserdata().getId());
                        //update.set("createAt", LocalDateTime.now());

                        return mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true).upsert(true), ProductModel.class)
                                .subscribeOn(Schedulers.boundedElastic())
                                .map(productModel1 -> {
                                    log.info("raisePrice user : "+ productModel1.getFinalBuyer());
                                    log.info("raisePrice + " + productModel1.getPrice());
                                    return new ResponseEntity<String>("success raise price " + productModel1.getPrice(),HttpStatus.OK);
                                });
                    });
        }

    }

    /**
     * 경매 상태 변경 메소드
     *
     *
     * */
    public Mono<ResponseEntity<String>> changeAuctionState(AuctionRaiseDto auctionRaiseDto){

        return productRepository.findById(auctionRaiseDto.getProduct().getId())
                .flatMap((productModel)->{

                    Query query = new Query();
                    query.addCriteria(Criteria.where("id").is(auctionRaiseDto.getProduct().getId()));

                    Update update = new Update();

                    // 현재 상태 변경
                    update.set("auctionState", !productModel.isAuctionState());

                    return mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true).upsert(true), ProductModel.class)
                            .subscribeOn(Schedulers.boundedElastic())
                            .map(productModel1 -> {
                                log.info("chage auction, now auction state : "+ productModel1.isAuction());
                                return new ResponseEntity<String>("success change auction, auction state : "+ productModel1.isAuction(),HttpStatus.OK);
                            });
                    });

    }

}
