package com.example.auctionchat.service;

import com.example.auctionchat.mongomodel.ChatModel;
import com.example.auctionchat.mongorepository.ChatModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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

    @Transactional(readOnly = true)
    public Mono<List<ChatModel>> findCheckRoom(int id){


        return chatModelRepository.findAllByRoomNum(id);
    }

    public void deleteRoom(int id){

        chatModelRepository.deleteAllByRoomNum(id);
    }

    @Transactional
    public Mono<ChatModel> makeRoom(ChatModel chatModel){
        log.info(chatModel);

        try {
            return chatModelRepository.save(chatModel);
        }catch (Exception e){
            return null;
        }

    }



}
