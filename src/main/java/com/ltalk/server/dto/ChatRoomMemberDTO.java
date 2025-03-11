package com.ltalk.server.dto;

import com.ltalk.server.entity.ChatRoomMember;
import com.ltalk.server.entity.Member;

public class ChatRoomMemberDTO {
    private Long id;
    private Long memberId;
    private Long chatRoomId;
    private String memberName;
    private Long readChatId;

    public ChatRoomMemberDTO(ChatRoomMember chatRoomMember){
        Member member =  chatRoomMember.getMember();
        id = chatRoomMember.getChatRoomMemberId();
        memberId = member.getId();
        chatRoomId = chatRoomMember.getChatRoom().getChatRoomId();
        memberName = member.getUsername();
        readChatId = chatRoomMember.getReadChatId();
    }
}
