package com.example.auctionchat.controller;

import com.example.auctionchat.mongomodel.ChatModel;
import com.example.auctionchat.mongomodel.ProductModel;
import com.example.auctionchat.mongomodel.Room;
import com.example.auctionchat.mongorepository.ChatModelRepository;
import com.example.auctionchat.service.ChatRoomService;
import com.example.auctionchat.service.ProductService;
import com.example.auctionchat.service.RoomService;
import com.mongodb.CursorType;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.sun.nio.sctp.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.mongo.ReactiveMongoClientFactory;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;



import java.time.Duration;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@RequestMapping(value = "auth")
@RestController
public class AllAccessibleController {

    private final ChatRoomService chatRoomService;

    private final RoomService roomService;

    private final ProductService productService;

    private final MongoClient mongoClient;



    @GetMapping("hello")
    public String testHello(){
        return "hello";
    }

    @GetMapping(value = "find-all-chat-room")
    public Mono<List<Room>> findAllChatRoomList(){

        return chatRoomService.findAllChatRoom();
    }

    // 참가는 자유롭게 가능 허나 메세지 보내는 것은 로그인후 가능
    //@GetMapping(value = "find-room/{roomNum}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ResponseEntity<ChatModel>> findRoomNum(@PathVariable Integer roomNum){

        //Room findRoom = roomService.roomCheck(roomNum).block();

        log.info("접속 요청 : "+ roomNum);
        //log.info("방 요청 결과  : "+ findRoom);


        return chatRoomService.requestRoom(roomNum)
                .subscribeOn(Schedulers.boundedElastic())
                .cache();
    }

    @GetMapping(value = "find-room/{roomNum}/check-video-url")
    public Mono<Room> findRoomVideoUrl(@PathVariable Integer roomNum){
        return roomService.roomCheck(roomNum);
    }


    @GetMapping(value = "find-product/{roomNum}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ResponseEntity<ProductModel>> findProduct(@PathVariable Integer roomNum){

        return productService.findListProduct(roomNum)
                .map(productModel -> new ResponseEntity<>(productModel, HttpStatus.OK))
                .subscribeOn(Schedulers.boundedElastic());
    }

    private Flux<ServerSentEvent<Notification>> getHeartbeatStream() {

        return Flux.interval(Duration.ofSeconds(2))
                .map(i -> ServerSentEvent.<Notification>builder().event("ping").build())
                .doFinally(signalType -> {
                    log.info("END");
                    log.info(signalType);
                });
    }

    private Flux<Object> getEventMessageStream(Integer roomNum) {

        return chatRoomService.requestRoom(roomNum)
                .subscribeOn(Schedulers.boundedElastic())
                .filter(data -> data.getBody() != null)
                .map(data -> ServerSentEvent
                        .builder(data)
                        .event("message").build());
    }

    @GetMapping(value = "find-room/{roomNum}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Object> findRoomNumMerge(@PathVariable Integer roomNum){

        //Room findRoom = roomService.roomCheck(roomNum).block();

        log.info("접속 요청 : "+ roomNum);
        //log.info("방 요청 결과  : "+ findRoom);
        return Flux.merge(getEventMessageStream(roomNum), getHeartbeatStream())
                .subscribeOn(Schedulers.boundedElastic())
                .doFinally(signalType -> {
                    log.info("END");
                    log.info(signalType);
                    log.info("채팅방 종료");
                });
    }


}
