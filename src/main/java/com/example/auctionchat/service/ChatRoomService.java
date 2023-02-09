package com.example.auctionchat.service;


import com.example.auctionchat.model.ChatRoomModel;
import com.example.auctionchat.mongomodel.ChatModel;
import com.example.auctionchat.mongomodel.Room;
import com.example.auctionchat.mongorepository.ChatModelRepository;
import com.example.auctionchat.mongorepository.RoomRepositry;
import com.example.auctionchat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.CoreSubscriber;
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

    private final RoomRepositry roomRepositry;

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
    public Flux<ChatModel> requestRoom(int roomNum){
        return chatModelRepository.findByRoomNum(roomNum);
    }

    @Transactional
    public Mono<ChatModel> sendMsg(ChatModel chatModel){

        // 룸이있나 검사
        Room room = roomRepositry.roomCheck(chatModel.getRoomNum()).block();

        if(room != null){
            log.info("save message : "+ chatModel.getMsg());
            return chatModelRepository.save(chatModel);
        }else {
            log.info("not found room: "+ chatModel.getMsg());
            return null;
        }
    }

    @Transactional(readOnly = true)
    public Mono<List<ChatModel>> findAllChatRoom(){




        return null;
    }

}
