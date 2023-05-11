package com.example.auctionchat.mongomodel;


import lombok.Data;
import org.joda.time.LocalDateTime;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;

//import org.joda.time.LocalDateTime; // 바뀐부분

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

    private int chiefId;
    private String chief;

    private LocalDateTime createAt;
}
