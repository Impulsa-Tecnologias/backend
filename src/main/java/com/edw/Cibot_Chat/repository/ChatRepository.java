package com.edw.Cibot_Chat.repository;

import com.edw.Cibot_Chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findByUserIdOrderByCreatedAtDesc(Long userId);

    long countByUserId(Long userId);

    boolean existsByIdAndUserId(Long id, Long userId);
}
