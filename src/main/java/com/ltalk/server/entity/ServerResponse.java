package com.ltalk.server.entity;

import com.ltalk.server.enums.ProtocolType;
import com.ltalk.server.response.ChatResponse;
import com.ltalk.server.response.LoginResponse;
import com.ltalk.server.response.SignupResponse;
import lombok.Getter;

@Getter
public class ServerResponse {
    private ProtocolType protocolType;
    private boolean success;
    private ChatResponse chatResponse;
    private LoginResponse loginResponse;
    private SignupResponse signupResponse;

    public ServerResponse(ProtocolType protocolType, boolean success, ChatResponse chatResponse) {
        this.protocolType = protocolType;
        this.success = success;
        this.chatResponse = chatResponse;
    }
    public ServerResponse(ProtocolType protocolType, boolean success, LoginResponse loginResponse) {
        this.protocolType = protocolType;
        this.success = success;
        this.loginResponse = loginResponse;
    }
    public ServerResponse(ProtocolType protocolType, boolean success, SignupResponse signupResponse) {
        this.protocolType = protocolType;
        this.success = success;
        this.signupResponse = signupResponse;
    }

}
