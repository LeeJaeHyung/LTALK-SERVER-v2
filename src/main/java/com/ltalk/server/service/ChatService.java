package com.ltalk.server.service;

import com.ltalk.server.entity.ChatRoom;
import com.ltalk.server.entity.ChatRoomMember;
import com.ltalk.server.entity.Member;
import com.ltalk.server.repository.ChatRoomMemberRepository;
import com.ltalk.server.repository.ChatRoomRepository;
import com.ltalk.server.request.ChatRequest;
import com.ltalk.server.request.ChatRoomCreatRequest;

import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;
import java.util.List;

public class ChatService {

    AsynchronousSocketChannel channel;
    ChatRoomRepository chatRoomRepository;
    ChatRoomMemberRepository chatRoomMemberRepository;
    public ChatService(AsynchronousSocketChannel socketChannel) {
        channel = socketChannel;
        this.chatRoomRepository = new ChatRoomRepository();
        this.chatRoomMemberRepository = new ChatRoomMemberRepository();
    }


    public void chat(ChatRequest chatRequest) {
        String message = chatRequest.getMessage();

    }

    public void creatChatRoom(ChatRoomCreatRequest chatRoomCreatRequest){
        ChatRoom chatRoom = new ChatRoom(chatRoomCreatRequest);
        chatRoomRepository.save(chatRoom);
        List<String> chatRoomMembers = chatRoomCreatRequest.getChatRoomMembers();
        List<ChatRoomMember> chatRoomMemberList = new ArrayList<>();
        MemberService memberService = new MemberService(channel);
        for(String userName : chatRoomMembers){
            Member member = memberService.findByUserName(userName);
            ChatRoomMember chatRoomMember = new ChatRoomMember(member, chatRoom);
            chatRoomMemberRepository.save(chatRoomMember);
            chatRoomMemberList.add(chatRoomMember);
        }
        chatRoom.setParticipantCount(chatRoomMemberList.size());
        chatRoomRepository.update(chatRoom);
    }
}
