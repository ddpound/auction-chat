package com.example.auctionchat.mongomodel;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Document(collection = "chatModel")
public class ChatModel {
    @Id
    private String id;
    private String msg;
    private String sender;
    private String receiver; //(귓속말)

    @DocumentReference
    private Room room;

    private LocalDateTime createAt;
}
