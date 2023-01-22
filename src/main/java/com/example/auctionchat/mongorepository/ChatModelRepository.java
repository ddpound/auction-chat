package com.example.auctionchat.mongorepository;

import com.example.auctionchat.mongomodel.ChatModel;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;

public interface ChatModelRepository extends ReactiveMongoRepository<ChatModel, String> {

    @Tailable
    @Query("{sender: ?0,receiver: ?1}")
    Flux<ChatModel> mFindBySender(String sender, String receiver); // flux 흐름, response를 유지하면서 데이터 계속 흘려보내기
}
