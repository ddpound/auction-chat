package com.example.auctionchat.controller;

import com.example.auctionchat.model.ChatRoomModel;
import com.example.auctionchat.mongomodel.ChatModel;
import com.example.auctionchat.mongomodel.Room;
import com.example.auctionchat.service.ChatRoomService;
import com.example.auctionchat.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@RequestMapping(value = "auth")
@RestController
public class AllAccessibleController {

    private final ChatRoomService chatRoomService;

    private final RoomService roomService;


    @GetMapping("hello")
    public String testHello(){
        return "hello";
    }

    @GetMapping(value = "find-all-chat-room")
    public Mono<List<ChatModel>> findAllChatRoomList(){

        return chatRoomService.findAllChatRoom();
    }

    // 참가는 자유롭게 가능 허나 메세지 보내는 것은 로그인후 가능
    @GetMapping(value = "find-room/{roomNum}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatModel> findRoomNum(@PathVariable Integer roomNum){

        //Room findRoom = roomService.roomCheck(roomNum).block();

        log.info("접속 요청 : "+ roomNum);
        //log.info("방 요청 결과  : "+ findRoom);

        return chatRoomService.requestRoom(roomNum).subscribeOn(Schedulers.boundedElastic());
    }


}
