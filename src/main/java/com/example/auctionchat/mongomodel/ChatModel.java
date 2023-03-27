package com.example.auctionchat.mongomodel;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.repository.Meta;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "chatModel")
public class ChatModel {

    @Id
    private String id;
    private String msg;
    // sender는 구글 계정의 nickName을 사용한다.
    private String sender;
    private String receiver; //(귓속말)

    private String profile;

    private int roomNum;

    private LocalDateTime createAt;
}
