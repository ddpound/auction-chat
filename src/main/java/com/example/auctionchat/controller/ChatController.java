package com.example.auctionchat.controller;

import com.example.auctionchat.mongomodel.ChatModel;
import com.example.auctionchat.service.ChatRoomService;
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
@RequestMapping("auth")
public class ChatController {

    private final ChatRoomService chatRoomService;

    @GetMapping(value = "sender/{sender}/receiver/{receiver}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatModel> getMsg(@PathVariable String sender , @PathVariable String receiver){
        return chatRoomService.getMsg(sender,receiver);
    }

    @PostMapping("chat")
    public Mono<ChatModel> setMsg(@RequestBody ChatModel chatModel){

        System.out.println(chatModel);
        return chatRoomService.setMsg(chatModel);
    }

}
