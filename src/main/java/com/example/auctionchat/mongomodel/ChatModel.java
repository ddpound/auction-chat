package com.example.auctionchat.mongomodel;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Document(collection = "chatModel")
public class ChatModel {
    @Id
    private String id;
    private String msg;
    private String sender;
    private String receiver;

    private LocalDateTime createAt;
}
