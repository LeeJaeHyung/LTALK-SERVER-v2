package com.ltalk.server.response;

import com.ltalk.server.dto.ChatDTO;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NewChatResponse {
    ChatDTO dto;
    public NewChatResponse(ChatDTO dto) {
        this.dto = dto;
    }
}
