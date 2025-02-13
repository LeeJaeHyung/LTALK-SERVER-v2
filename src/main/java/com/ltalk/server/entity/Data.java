package com.ltalk.server.entity;

import com.ltalk.server.enums.ProtocolType;
import com.ltalk.server.request.ChatRequest;
import com.ltalk.server.request.LoginRequest;
import com.ltalk.server.request.SignupRequest;
import lombok.Getter;

@Getter
public class Data {
    ProtocolType protocolType;
    ChatRequest chatRequest;
    LoginRequest loginRequest;
    SignupRequest signupRequest;
    String username;
}
