package com.ltalk.server.entity;

import com.ltalk.server.enums.ProtocolType;
import com.ltalk.server.request.*;
import lombok.Getter;

@Getter
public class Data {
    ProtocolType protocolType;
    ChatRequest chatRequest;
    LoginRequest loginRequest;
    SignupRequest signupRequest;
    DisconnectRequest disconnectRequest;
    FriendRequest friendRequest;
    ChatRoomCreatRequest chatRoomCreatRequest;
    ReadChatRequest readChatRequest;
    String username;
    public Data(ProtocolType protocolType) {
        this.protocolType = protocolType;
    }
}
