package com.example.auctionchat.controller;


import com.example.auctionchat.mongomodel.ChatModel;
import com.example.auctionchat.mongomodel.Room;
import com.example.auctionchat.service.ChatRoomService;
import com.example.auctionchat.service.RoomService;
import com.example.auctionchat.service.SellerChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.Random;


/**
 * 판매자가 직접 만드는 방입니다.
 * */
@RequiredArgsConstructor
@Log4j2
@RequestMapping(value = "seller")
@RestController
public class SellerChatController {

    private final ChatRoomService chatRoomService;

    private final SellerChatService sellerChatService;

    private final RoomService roomService;

    @PostMapping(value = "make-room")
    public Mono<Room> makeChatRoom(Room room){
        Random random = new Random();
        int makeRoomNum = 1;

        // 방 중복 테스트
        while(true){
            Mono<Room> searchChatRoom = roomService.roomCheck(makeRoomNum);
            log.info(searchChatRoom.block());

            // 적어도 방한개는 있다는 뜻
            if (searchChatRoom.block() != null){
                makeRoomNum = random.nextInt(1000);
                log.info("제작 방 : "+ makeRoomNum);
            }else{
                break;
            }
        }


        room.setRoomNum(makeRoomNum);

        return sellerChatService.makeRoom(room);
    }

    @DeleteMapping(value = "delete-room")
    public String deleteChatRoom(@RequestBody ChatModel chatModel){


        return "success delete room";
    }


}
