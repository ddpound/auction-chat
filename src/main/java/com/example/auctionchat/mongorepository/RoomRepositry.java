package com.example.auctionchat.mongorepository;

import com.example.auctionchat.mongomodel.ChatModel;
import com.example.auctionchat.mongomodel.Room;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoomRepositry extends ReactiveMongoRepository<Room,Integer> {


    @Tailable
    @Query("{roomNum: ?0}")
    Flux<Room> findByRoomNum(int roomNum);

    @Query("{roomNum: ?0}")
    Mono<Room> roomCheck(Integer roomNum);

}
