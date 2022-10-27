package com.example.auctionchat.repository;

import com.example.auctionchat.model.ChatRoomModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoomModel,Integer> {
}
