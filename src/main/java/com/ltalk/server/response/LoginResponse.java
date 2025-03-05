package com.ltalk.server.response;

import com.ltalk.server.dto.ChatRoomDTO;
import com.ltalk.server.dto.FriendDTO;
import com.ltalk.server.entity.Member;
import lombok.Getter;

import java.util.List;

@Getter
public class LoginResponse {
    private String msg;
    private Member member;
    private List<FriendDTO> friends;
    private List<ChatRoomDTO> chatRoom;

    public LoginResponse(String msg){
        this.msg = msg;
    }
    public LoginResponse(Member member, List<FriendDTO> friendList, List<ChatRoomDTO> chatRoom, String msg) {
        this.member = member;
        this.msg = msg;
        this.friends = friendList;
        this.chatRoom = chatRoom;
    }
}
