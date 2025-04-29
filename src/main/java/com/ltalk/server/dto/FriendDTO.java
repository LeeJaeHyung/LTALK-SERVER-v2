package com.ltalk.server.dto;

import com.ltalk.server.entity.Friend;
import com.ltalk.server.entity.Member;
import com.ltalk.server.enums.FriendStatus;
import lombok.Setter;

@Setter
public class FriendDTO {
    private Long friendId;
    private String friendName;
    private FriendStatus status;

    public FriendDTO(Friend friend){
        friendId = friend.getId();
        friendName = friend.getFriend().getUsername();
        status = friend.getStatus();
    }

    public FriendDTO(Member member){
        friendId = member.getId();
        friendName = member.getUsername();
    }
}
