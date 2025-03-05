package com.ltalk.server.dto;

import com.ltalk.server.entity.ChatRoomMember;

public class ChatRoomMemberDTO {
    private Long id;
    private Long memberId;
    private String memberName;
    private Long readChatId;

    public ChatRoomMemberDTO(ChatRoomMember chatRoomMember){
        id = chatRoomMember.getChatRoomMemberId();
        memberId = chatRoomMember.getChatRoomMemberId();
        memberName = chatRoomMember.getMember().getUsername();
        readChatId = chatRoomMember.getReadChatId();
    }
}
