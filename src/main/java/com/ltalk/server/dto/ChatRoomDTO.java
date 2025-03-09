package com.ltalk.server.dto;

import com.ltalk.server.entity.ChatRoom;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChatRoomDTO {
    private Long id;
    private String name;
    private LocalDateTime lastChattedAt;
    private Integer participantCount = 0;
    private List<ChatRoomMemberDTO> members = new ArrayList<>();
    private List<ChatDTO> chats = new ArrayList<>();

    public ChatRoomDTO(ChatRoom chatRoom) {
        id = chatRoom.getChatRoomId();
        name = chatRoom.getName();
        lastChattedAt = chatRoom.getLastChattedAt();
        participantCount = chatRoom.getParticipantCount();
        chatRoom.getMembers().forEach(m -> members.add(new ChatRoomMemberDTO(m)));
        chatRoom.getChats().forEach(c -> System.out.println(c.getChatId()));
        chatRoom.getChats().forEach(c -> chats.add(new ChatDTO(c, id)));
    }
}
