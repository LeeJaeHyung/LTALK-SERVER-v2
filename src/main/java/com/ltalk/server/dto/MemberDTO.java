package com.ltalk.server.dto;

import com.ltalk.server.entity.Member;

import java.util.ArrayList;
import java.util.List;

public class MemberDTO {

    private Long id;
    private String username;
    private String email;
    private List<FriendDTO> friends;
    private List<ChatRoomDTO> chatRoom;

    public MemberDTO(Member member, List<FriendDTO> friends, List<ChatRoomDTO> chatRoom) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.friends = friends;
        this.chatRoom = chatRoom;
    }
}
