package com.ltalk.server.response;

import com.ltalk.server.request.ChatRequest;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatResponse {
    private Long chatRoomId;
    private Long senderId;
    private String message;
    private LocalDateTime sendDate;
    public ChatResponse(ChatRequest chatRequest) {
        this.chatRoomId = chatRequest.getChatRoomId();//chatRequest.getReceiver();
        this.senderId = chatRequest.getSenderId();
        this.message = chatRequest.getMessage();
        this.sendDate = chatRequest.getSendDate();
    }
}
