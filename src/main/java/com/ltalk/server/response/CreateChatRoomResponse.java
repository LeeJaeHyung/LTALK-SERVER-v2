package com.ltalk.server.response;

import com.ltalk.server.dto.ChatRoomDTO;

public class CreateChatRoomResponse {
    ChatRoomDTO chatRoomDTO;
    public CreateChatRoomResponse(ChatRoomDTO chatRoomDTO) {
        this.chatRoomDTO = chatRoomDTO;
    }
}
