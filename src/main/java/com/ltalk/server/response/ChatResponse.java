package com.ltalk.server.response;

import com.ltalk.request.ChatRequest;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatResponse {
    private String receiver;
    private String sender;
    private String message;
    private LocalDateTime sendDate;
    public ChatResponse(ChatRequest chatRequest) {
        this.receiver = null;//chatRequest.getReceiver();
        this.sender = chatRequest.getSender();
        this.message = chatRequest.getMessage();
        this.sendDate = chatRequest.getSendDate();
    }
}
