package com.ltalk.server.response;

import com.ltalk.server.dto.ChatRoomMemberDTO;
import lombok.Getter;

@Getter
public class ReadChatResponse {

    ChatRoomMemberDTO roomMember;

    public ReadChatResponse(ChatRoomMemberDTO chatRoomMemberDTO) {
        this.roomMember = chatRoomMemberDTO;
    }
}
