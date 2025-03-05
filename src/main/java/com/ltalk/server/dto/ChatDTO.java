package com.ltalk.server.dto;

import com.ltalk.server.entity.Chat;

import java.time.LocalDateTime;


public class ChatDTO {
    private long chatId;
    private String sender;
    private String message;
    private LocalDateTime createdAt;

    public ChatDTO (Chat chat){
        chatId = chat.getChatId();
        sender = chat.getSender().getUsername();
        message = chat.getMessage();
        createdAt = chat.getCreatedAt();
    }
}
