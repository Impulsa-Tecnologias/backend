package com.edw.Cibot_Chat.dto.response;

import com.edw.Cibot_Chat.enums.MessageSender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {

    private Long id;
    private Long chatId;
    private MessageSender sender;
    private String content;
    private LocalDateTime sendDate;
}
