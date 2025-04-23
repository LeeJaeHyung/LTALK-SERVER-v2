package com.ltalk.server.request;

import com.ltalk.server.enums.ChatRoomType;
import lombok.Getter;

import java.util.List;

@Getter
public class ChatRoomCreatRequest {
    private String roomName;
    private ChatRoomType roomType;
    private List<String> chatRoomMembers;

    public ChatRoomCreatRequest(String roomName, ChatRoomType roomType, List<String> chatRoomMembers) {
        this.roomName = roomName;
        this.roomType = roomType;
        this.chatRoomMembers = chatRoomMembers;
    }
}
