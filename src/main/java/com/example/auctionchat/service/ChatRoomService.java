package com.example.auctionchat.service;


import com.example.auctionchat.mongomodel.ChatModel;
import com.example.auctionchat.mongomodel.Room;
import com.example.auctionchat.mongorepository.ChatModelRepository;
import com.example.auctionchat.mongorepository.RoomRepositry;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Log4j2
@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatModelRepository chatModelRepository;

    private final RoomRepositry roomRepositry;


    @Transactional(readOnly = true)
    public Flux<ChatModel> whispering(String sender, String receiver){
        return chatModelRepository.mFindBySender(sender,receiver)
                .subscribeOn(Schedulers.boundedElastic());
    }

    //@Transactional(readOnly = true)
    public Flux<ResponseEntity<ChatModel>> requestRoom(int roomNum) {

        return chatModelRepository
                .findByRoomNum(roomNum)
                .map(chatModel -> new ResponseEntity<>(chatModel, HttpStatus.OK))
                .subscribeOn(Schedulers.boundedElastic())
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
        return roomRepositry.findAll().collectList();
    }

}
