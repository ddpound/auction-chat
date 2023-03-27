package com.example.auctionchat.models;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 채팅방의 유저 리스트
 * */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class ChatRoomUserList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;



}
