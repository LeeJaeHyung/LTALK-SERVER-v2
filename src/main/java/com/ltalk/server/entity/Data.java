package com.ltalk.server.entity;

import com.ltalk.enums.ProtocolType;
import com.ltalk.request.ChatRequest;
import com.ltalk.request.LoginRequest;
import com.ltalk.request.SignupRequest;
import lombok.Getter;

@Getter
public class Data {
    ProtocolType protocolType;
    ChatRequest chatRequest;
    LoginRequest loginRequest;
    SignupRequest signupRequest;
    String username;
}
