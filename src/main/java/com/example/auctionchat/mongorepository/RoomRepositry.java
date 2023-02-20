package com.example.auctionchat.mongorepository;

import com.example.auctionchat.mongomodel.ChatModel;
import com.example.auctionchat.mongomodel.DeleteResult;
import com.example.auctionchat.mongomodel.Room;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;

public interface RoomRepositry extends ReactiveMongoRepository<Room,Integer> {


    @Query("{roomNum: ?0}")
    Flux<Room> findByRoomNum(int roomNum);

    @Query("{roomNum: ?0}")
    Mono<Room> roomCheck(Integer roomNum);

    // 방장찾기
    Mono<Room> findByChief(String chief);

    @Query("{chief: ?0}")
    Flux<Room> findAllByChief(String chief);

    //@Query(value = "{chief : ?0}" ,delete = true)
    Flux<List<Room>> deleteRoomByChief(String chief);

    Flux<List<Room>> deleteByRoomNum(int roomNum);


}
