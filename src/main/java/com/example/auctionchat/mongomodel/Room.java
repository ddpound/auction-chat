package com.example.auctionchat.mongomodel;


import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Data
@Document(collection = "room")
public class Room {

    @Id
    private String id;

    @Indexed(unique = true)
    private int roomNum;

    private String roomTitle;

    private String videoUrl;
    private String thumbnail;
    private String chief;
}
