package com.example.auctionchat.controller;

import com.example.auctionchat.mongomodel.Room;
import com.example.auctionchat.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Log4j2
@RequestMapping(value = "seller")
@RestController
public class SellerRoomController {

    private final RoomService roomService;


    @PutMapping(value = "room-change-url")
    public Mono<Room> changeRoomVideoUrl(Room room){

        return roomService.roomVideoUrlChange(room);
    }

}
