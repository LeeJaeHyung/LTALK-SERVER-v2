package com.ltalk.server.response;

import lombok.Getter;

@Getter
public class VoiceServerIPResponse {

    String ip;
    final int port = 7878;
    Long chatRoomId;

    public VoiceServerIPResponse(String ip, Long chatRoomId) {
        this.ip = ip;
        this.chatRoomId = chatRoomId;
    }
}
