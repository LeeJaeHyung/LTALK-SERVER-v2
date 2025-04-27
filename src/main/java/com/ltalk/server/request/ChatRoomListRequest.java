package com.ltalk.server.request;

import lombok.Getter;

@Getter
public class ChatRoomListRequest {
    Long memberId;
    public ChatRoomListRequest(Long memberId) {
        this.memberId = memberId;
    }
}
