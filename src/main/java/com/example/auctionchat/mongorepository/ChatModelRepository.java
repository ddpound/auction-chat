package com.example.auctionchat.mongorepository;

import com.example.auctionchat.mongomodel.ChatModel;
import com.example.auctionchat.mongomodel.DeleteResult;
import com.example.auctionchat.mongomodel.Room;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.mongodb.repository.*;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


public interface ChatModelRepository extends ReactiveMongoRepository<ChatModel, String> {



    Mono<List<ChatModel>> findDistinctByRoomNum(String key);


    // 귓속말
    @Tailable
    @Query("{sender: ?0,receiver: ?1}")
    Flux<ChatModel> mFindBySender(String sender, String receiver); // flux 흐름, response를 유지하면서 데이터 계속 흘려보내기

    @Tailable
    @Query("{roomNum : ?0}")
    Flux<ChatModel> findByRoomNum(int roomNum);

    @Query("{roomNum : ?0}")
    Flux<ChatModel> findNonTailableByRoomNum(int roomNum);


}
