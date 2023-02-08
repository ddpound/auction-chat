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

@Log4j2
@RequiredArgsConstructor
@Service
public class SellerChatService {

    private final ChatModelRepository chatModelRepository;

    private final RoomRepositry roomRepositry;

    @Transactional
    public Mono<Room> makeRoom(Room room){
        log.info(room);

        try {
            return roomRepositry.save(room);
        }catch (Exception e){
            return null;
        }

    }



}
