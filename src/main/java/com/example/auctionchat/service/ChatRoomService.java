package com.example.auctionchat.service;


import com.example.auctionchat.mongomodel.ChatModel;
import com.example.auctionchat.mongomodel.Room;
import com.example.auctionchat.mongorepository.ChatModelRepository;
import com.example.auctionchat.mongorepository.RoomRepositry;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


import org.joda.time.LocalDateTime;
import org.springframework.data.mongodb.core.ReactiveFindOperation;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;


import java.util.List;
import java.util.Objects;


@Log4j2
@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatModelRepository chatModelRepository;

    private final RoomRepositry roomRepositry;


    // @tailable 어노테이션의 문제점 발견, 템플레이트를 통해 접근하기로함
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    //@Transactional(readOnly = true)
    public Flux<ChatModel> whispering(String sender, String receiver){
        return chatModelRepository.mFindBySender(sender,receiver)
                .subscribeOn(Schedulers.boundedElastic());
    }


    public Flux<ResponseEntity<ChatModel>> requestRoom(int roomNum) {

        Query query = new Query(Criteria.where("roomNum").is(roomNum));

        ReactiveFindOperation.TerminatingFind<ChatModel> find = reactiveMongoTemplate
                .query(ChatModel.class)
                .matching(query);


//        return chatModelRepository
//                .findByRoomNum(roomNum)
//                .map(chatModel -> new ResponseEntity<>(chatModel, HttpStatus.OK))
//                .subscribeOn(Schedulers.boundedElastic())
//                .switchIfEmpty(Mono.defer(()-> Mono.just(new ResponseEntity<>(null, HttpStatus.BAD_REQUEST))));

        return find.tail()
                .map(chatModel -> new ResponseEntity<>(chatModel, HttpStatus.OK))
                .switchIfEmpty(Mono.defer(()-> Mono.just(new ResponseEntity<>(null, HttpStatus.BAD_REQUEST))));
    }




    public Mono<ChatModel> sendMsg(ChatModel chatModel){

        // 룸이있나 검사
        Room room = roomRepositry.roomCheck(chatModel.getRoomNum())
                .subscribeOn(Schedulers.immediate()).block();



        if(room != null){
            log.info("save message : "+ chatModel.getMsg());

            chatModel.setCreateAt(LocalDateTime.now());
            return chatModelRepository.save(chatModel);
        }else {
            log.info("not found room: "+ chatModel.getMsg());
            return null;
        }
    }


    public Mono<List<Room>> findAllChatRoom(){
        return roomRepositry
                .findAll().collectList();
    }

}
