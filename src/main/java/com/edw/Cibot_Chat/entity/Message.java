package com.edw.Cibot_Chat.entity;

import com.edw.Cibot_Chat.enums.MessageSender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private MessageSender sender;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "send_date", updatable = false)
    private LocalDateTime sendDate;

    @PrePersist
    protected void onCreate() {
        sendDate = LocalDateTime.now();
    }
}
