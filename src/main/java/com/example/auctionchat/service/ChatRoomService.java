package com.example.auctionchat.service;


import com.example.auctionchat.model.ChatRoomModel;
import com.example.auctionchat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

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
    public List<ChatRoomModel> findAllChatRoom(){
        // 근데 또 비밀번호를 빼줘야함 이게 아주 골치아픈 상황임
        return chatRoomRepository.findAll();
    }

}
