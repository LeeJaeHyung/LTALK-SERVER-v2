package com.ltalk.server.service;

import com.ltalk.server.controller.ServerController;
import com.ltalk.server.dto.ChatDTO;
import com.ltalk.server.entity.*;
import com.ltalk.server.enums.ProtocolType;
import com.ltalk.server.repository.ChatRepository;
import com.ltalk.server.repository.ChatRoomMemberRepository;
import com.ltalk.server.repository.ChatRoomRepository;
import com.ltalk.server.request.ChatRequest;
import com.ltalk.server.request.ChatRoomCreatRequest;
import com.ltalk.server.response.NewChatResponse;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class ChatService {

    AsynchronousSocketChannel channel;
    ChatRepository chatRepository;
    ChatRoomRepository chatRoomRepository;
    ChatRoomMemberRepository chatRoomMemberRepository;

    public ChatService(AsynchronousSocketChannel socketChannel) {
        channel = socketChannel;
        this.chatRepository = new ChatRepository();
        this.chatRoomRepository = new ChatRoomRepository();
        this.chatRoomMemberRepository = new ChatRoomMemberRepository();
    }


    public void chat(ChatRequest chatRequest) throws NoSuchAlgorithmException, IOException {
        Member member = new MemberService(channel).findById(chatRequest.getSenderId());
        ChatRoom chatRoom = new ChatService(channel).roomFindById(chatRequest.getChatRoomId());
        Chat chat = new Chat(chatRoom, member, chatRequest.getMessage());
        chatRepository.save(chat);

        String senderName = member.getUsername();
        for(ChatRoomMember chatRoomMember : chatRoom.getMembers()){
            String userName = chatRoomMember.getMember().getUsername();
            if(userName.equals(senderName)){
                continue;
            }
            Client client = ServerController.clients.get(userName);
            if(client != null){
                AsynchronousSocketChannel socketChannel = client.getSocketChannel();
                DataService dataService = new DataService(socketChannel);
                dataService.send(new ServerResponse(ProtocolType.NEW_CHAT, true, new NewChatResponse(new ChatDTO(chat, chatRequest.getChatRoomId()))));
            }
        }
    }

    private ChatRoom roomFindById(Long chatRoomId) {
        return chatRoomRepository.roomFindById(chatRoomId);
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
