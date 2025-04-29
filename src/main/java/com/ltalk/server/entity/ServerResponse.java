package com.ltalk.server.entity;

import com.ltalk.server.enums.ProtocolType;
import com.ltalk.server.response.*;
import lombok.Getter;

@Getter
public class ServerResponse {
    private ProtocolType protocolType;
    private boolean status;
    private ChatResponse chatResponse;
    private LoginResponse loginResponse;
    private SignupResponse signupResponse;
    private NewChatResponse newChatResponse;
    private ReadChatResponse readChatResponse;
    private VoiceServerIPResponse voiceServerIPResponse;
    private CreateVoiceMemberResponse createVoiceMemberResponse;
    private FriendSearchResponse friendSearchResponse;
    private ChatRoomCreationCheckResponse chatRoomCreationCheckResponse;
    private CreateChatRoomResponse createChatRoomResponse;
    private RequestFriendResponse requestFriendResponse;

    public ServerResponse(ProtocolType protocolType, boolean status, ChatResponse chatResponse) {
        this.protocolType = protocolType;
        this.status = status;
        this.chatResponse = chatResponse;
    }
    public ServerResponse(ProtocolType protocolType, boolean status, LoginResponse loginResponse) {
        this.protocolType = protocolType;
        this.status = status;
        this.loginResponse = loginResponse;
    }
    public ServerResponse(ProtocolType protocolType, boolean status, SignupResponse signupResponse) {
        this.protocolType = protocolType;
        this.status = status;
        this.signupResponse = signupResponse;
    }
    public ServerResponse(ProtocolType protocolType, boolean status, NewChatResponse newChatResponse ) {
        this.protocolType = protocolType;
        this.status = status;
        this.newChatResponse = newChatResponse;
    }

    public ServerResponse(ProtocolType protocolType, boolean status, ReadChatResponse readChatResponse) {
        this.protocolType = protocolType;
        this.status = status;
        this.readChatResponse = readChatResponse;
    }

    public ServerResponse(VoiceServerIPResponse voiceServerIPResponse, boolean status) {
        this.protocolType = ProtocolType.GET_VOICE_SERVER_IP;
        this.status = status;
        this.voiceServerIPResponse = voiceServerIPResponse;
    }

    public ServerResponse(CreateVoiceMemberResponse createVoiceMemberResponse, boolean status) {
        this.protocolType = ProtocolType.RESPONSE_CREATE_CHATROOM_MEMBER;
        this.createVoiceMemberResponse = createVoiceMemberResponse;
        this.status = status;
    }

    public ServerResponse(FriendSearchResponse friendSearchResponse) {
        this.protocolType = ProtocolType.FRIEND_SEARCH;
        this.friendSearchResponse = friendSearchResponse;
    }

    public ServerResponse(ChatRoomCreationCheckResponse chatRoomCreationCheckResponse) {
        this.protocolType = ProtocolType.CAN_CREATE_CHAT_ROOM;
        this.chatRoomCreationCheckResponse = chatRoomCreationCheckResponse;
    }

    public ServerResponse(ProtocolType protocolType, CreateChatRoomResponse createChatRoomResponse) {
        this.protocolType = protocolType;
        this.createChatRoomResponse = createChatRoomResponse;
    }

    public ServerResponse(RequestFriendResponse requestFriendResponse) {
        this.protocolType = ProtocolType.REQUEST_FRIEND;
        this.requestFriendResponse = requestFriendResponse;
    }
}
