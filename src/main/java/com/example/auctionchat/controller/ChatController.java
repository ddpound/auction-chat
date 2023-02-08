package com.example.auctionchat.controller;

import com.example.auctionchat.mongomodel.ChatModel;
import com.example.auctionchat.mongomodel.Room;
import com.example.auctionchat.service.ChatRoomService;
import com.example.auctionchat.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Log4j2
@RestController
// 임시로 테스트를위해 일단 auth를 붙임
@RequestMapping("user")
public class ChatController {

    private final ChatRoomService chatRoomService;

    private final RoomService roomService;


    // 귓속말 용도
    @GetMapping(value = "sender/{sender}/receiver/{receiver}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatModel> whispering(@PathVariable String sender , @PathVariable String receiver){
        return chatRoomService.whispering(sender,receiver);
    }


    // 방을 찾아가고 DB에 메세지와 자기 이름 저장하는 것까지는 유저가 할수있고
    // 방을 추가하는 것은 판매자만 가능
    @GetMapping(value = "chat/room/{roomNum}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatModel> findRoomNum(@PathVariable Integer roomNum){

        Room findRoom = roomService.findRoom(roomNum).block();

        return chatRoomService.requestRoom(findRoom);
    }

    @PostMapping("send-message")
    public Mono<ChatModel> send(ChatModel chatModel){

        System.out.println(chatModel);
        return chatRoomService.sendMsg(chatModel);
    }

}
