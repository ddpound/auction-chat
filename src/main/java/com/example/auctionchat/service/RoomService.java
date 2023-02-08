package com.example.auctionchat.service;


import com.example.auctionchat.mongomodel.Room;
import com.example.auctionchat.mongorepository.RoomRepositry;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Service
public class RoomService {

    private final RoomRepositry roomRepositry;

    public Mono<Room> findRoom(int id){
        return roomRepositry.findById(id);
    }

}
