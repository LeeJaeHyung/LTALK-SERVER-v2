package com.ltalk.server.dto;

import com.ltalk.server.entity.Chat;
import com.ltalk.server.entity.ChatRoom;

import java.time.LocalDateTime;


public class ChatDTO {
    private long chatRoomId;
    private long chatId;
    private Long senderId;
    private String sender;
    private String message;
    private LocalDateTime createdAt;

    public ChatDTO (Chat chat){
        chatId = chat.getChatId();
        sender = chat.getSender().getUsername();
        senderId = chat.getSender().getId();
        message = chat.getMessage();
        createdAt = chat.getCreatedAt();
    }

    public ChatDTO (Chat chat, Long chatRoomId) {
        this(chat);
        this.chatRoomId = chatRoomId;
    }
}
