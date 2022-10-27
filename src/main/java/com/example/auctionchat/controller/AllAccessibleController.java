package com.example.auctionchat.controller;

import com.example.auctionchat.model.ChatRoomModel;
import com.example.auctionchat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@RequestMapping(value = "auth")
@RestController
public class AllAccessibleController {

    private final ChatRoomService chatRoomService;

    @GetMapping("hello")
    public String testHello(){
        return "hello";
    }

    @GetMapping(value = "find-all-chat-room")
    public ResponseEntity<List<ChatRoomModel>> findAllChatRoomList(){

        return new ResponseEntity<>(chatRoomService.findAllChatRoom(), HttpStatus.OK);
    }



}
