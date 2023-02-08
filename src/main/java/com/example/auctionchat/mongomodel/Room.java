package com.example.auctionchat.mongomodel;


import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Data
@Document(collection = "room")
public class Room {

    @Id
    @Indexed(unique = true)
    private Integer roomNum;


    private String roomTitle;
    private String chief;
}
