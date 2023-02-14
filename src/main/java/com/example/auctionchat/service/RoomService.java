package com.example.auctionchat.service;


import com.example.auctionchat.mongomodel.Room;
import com.example.auctionchat.mongorepository.RoomRepositry;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Log4j2
@RequiredArgsConstructor
@Service
public class RoomService {

    private final RoomRepositry roomRepositry;

    public Mono<Room> roomCheck(int id){
        return roomRepositry.roomCheck(id);
    }

    public Mono<Room> findRoomChief(String chief){
        return roomRepositry.findByChief(chief);
    }

    public void deleteRoomByChief(String chief){
       roomRepositry.deleteAllByChief(chief).subscribe();

    }

    public Mono<Room> roomVideoUrlChange(Room room){

        Mono<Room> findRoom = roomRepositry.findById(room.getRoomNum());

        Objects.requireNonNull(findRoom.block()).setVideoUrl(room.getVideoUrl());

        return findRoom;
    }

}
