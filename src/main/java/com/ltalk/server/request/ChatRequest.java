package com.ltalk.server.request;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatRequest {
    private String chatRoomName;
    private String sender;
    private String message;
    private LocalDateTime sendDate;
}
