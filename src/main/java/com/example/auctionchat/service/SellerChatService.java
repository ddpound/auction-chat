package com.example.auctionchat.service;

import com.example.auctionchat.mongomodel.ChatModel;
import com.example.auctionchat.mongomodel.Room;
import com.example.auctionchat.mongorepository.ChatModelRepository;
import com.example.auctionchat.mongorepository.RoomRepositry;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;

@Log4j2
@RequiredArgsConstructor
@Service
public class SellerChatService {

    private final ChatModelRepository chatModelRepository;

    private final RoomService roomService;

    private final RoomRepositry roomRepositry;


    public Mono<Room> makeRoom(Room room){

        Random random = new Random();
        int makeRoomNum = 1;

        // 방 중복 테스트
        while(true){
            Mono<Room> searchChatRoom = roomService.roomCheck(makeRoomNum);
            log.info(searchChatRoom.block());

            // 적어도 방한개는 있다는 뜻
            if (searchChatRoom.block() != null){
                makeRoomNum = random.nextInt(1000);
            }else{
                log.info("제작 방 : "+ makeRoomNum);
                break;
            }

            Mono<Room> searchChatRoomByChief = roomService.findRoomChief(room.getChief());

            if(searchChatRoomByChief != null){
                log.info("이미 있는방으로,삭제후 새로만들겠습니다.");

                chatModelRepository.deleteAllByRoomNum(searchChatRoomByChief.blockOptional().get().getRoomNum()).subscribe();
                roomService.deleteRoomByChief(room.getChief());

            }

        }


        room.setRoomNum(makeRoomNum);

        try {
            return roomRepositry.save(room);
        }catch (Exception e){
            return null;
        }

    }



}
