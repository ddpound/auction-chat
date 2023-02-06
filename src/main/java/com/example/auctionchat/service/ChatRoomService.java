package com.example.auctionchat.service;


import com.example.auctionchat.model.ChatRoomModel;
import com.example.auctionchat.mongomodel.ChatModel;
import com.example.auctionchat.mongorepository.ChatModelRepository;
import com.example.auctionchat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    private final ChatModelRepository chatModelRepository;

    @Transactional
    public int chatRoomRegister(String chatRoomTitle,
                                HttpServletRequest request){


        ChatRoomModel chatRoomModel = ChatRoomModel.builder()
                .title(chatRoomTitle)
                .username("")
                .build();

        chatRoomRepository.save(chatRoomModel);

        return 1;
    }

    @Transactional(readOnly = true)
    public Flux<ChatModel> whispering(String sender, String receiver){
        return chatModelRepository.mFindBySender(sender,receiver)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Transactional(readOnly = true)
    public Flux<ChatModel> findRoom(Integer roomNum){
        return chatModelRepository.mFindByRoomNum(roomNum)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Transactional
    public Mono<ChatModel> sendMsg(@RequestBody ChatModel chatModel){


        return chatModelRepository.save(chatModel);
    }

    @Transactional(readOnly = true)
    public List<ChatRoomModel> findAllChatRoom(){
        // 근데 또 비밀번호를 빼줘야함 이게 아주 골치아픈 상황임
        return chatRoomRepository.findAll();
    }

}
