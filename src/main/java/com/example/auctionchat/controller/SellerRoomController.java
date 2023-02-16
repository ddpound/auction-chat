package com.example.auctionchat.controller;

import com.example.auctionchat.mongomodel.Room;
import com.example.auctionchat.service.RoomService;
import com.example.auctionchat.service.SellerChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Random;

@RequiredArgsConstructor
@Log4j2
@RequestMapping(value = "seller")
@RestController
public class SellerRoomController {

    private final RoomService roomService;

    private final SellerChatService sellerChatService;

    @PostMapping(value = "make-room")
    public Mono<Room> makeChatRoom(Room room){


        return sellerChatService.makeRoom(room);
    }


    @PutMapping(value = "room-change-url")
    public Mono<Room> changeRoomVideoUrl(Room room){

        return roomService.roomVideoUrlChange(room);
    }

}
