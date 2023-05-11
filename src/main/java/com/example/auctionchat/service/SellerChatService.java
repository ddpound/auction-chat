package com.example.auctionchat.service;

import com.example.auctionchat.mongomodel.ChatModel;
import com.example.auctionchat.mongomodel.Room;
import com.example.auctionchat.mongorepository.ChatModelRepository;
import com.example.auctionchat.mongorepository.RoomRepositry;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



import java.util.Objects;
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

        System.out.println("제작 요청한 룸 : "+ room);

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

        }

        // 룸이 여러개 이미, 만들어진 방장의 방이있었다면.
        Flux<Room> searchChatRoomByChief = roomService.findAllRoomChief(room.getChief());




        try {

            if(Objects.requireNonNull(searchChatRoomByChief.collectList().block()).size() > 0){
                log.info("이미 있는방으로,삭제후 새로만들겠습니다.");

                System.out.println("1 : "+searchChatRoomByChief.collectList().block().get(0).getRoomNum());
                System.out.println("2 : "+searchChatRoomByChief.collectList().block());

                // 방장이 만들었던 룸 전부 검색해 map
                // 방장의 각각의 룸에 있던 채팅기록 전부 삭제
                // 마지막으로 방 삭제
                searchChatRoomByChief.map(room1 -> {
                    Flux<ChatModel> searchChatModelByRoom = chatModelRepository.findNonTailableByRoomNum(room1.getRoomNum());

                    searchChatModelByRoom.map(chatModel -> chatModelRepository.delete(chatModel).subscribe()).subscribe();

                    return roomRepositry.delete(room1).subscribe();
                }).subscribe();
            }

            room.setRoomNum(makeRoomNum);

            room.setCreateAt(LocalDateTime.now());

            return roomRepositry.save(room);

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }




}
