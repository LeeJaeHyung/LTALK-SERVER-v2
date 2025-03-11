package com.ltalk.server.request;

import lombok.Getter;

@Getter
public class ReadChatRequest {
    Long chatRoomId;
    Long chatId;
    Long memberId;

    public ReadChatRequest(Long chatRoomId, Long chatId, Long memberId) {
        this.chatRoomId = chatRoomId;
        this.chatId = chatId;
        this.memberId = memberId;
    }
}
