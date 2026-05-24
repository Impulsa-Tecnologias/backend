package com.edw.Cibot_Chat.repository;

import com.edw.Cibot_Chat.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByChat_IdOrderBySendDateAsc(Long chatId);
}
