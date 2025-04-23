package com.ltalk.server.request;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatRequest {
    private Long chatRoomId;
    private Long senderId;
    private String message;
    private LocalDateTime sendDate;
}
