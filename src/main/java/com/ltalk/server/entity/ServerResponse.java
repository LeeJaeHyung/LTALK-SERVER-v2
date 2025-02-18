package com.ltalk.server.entity;

import com.ltalk.server.enums.ProtocolType;
import com.ltalk.server.response.ChatResponse;
import com.ltalk.server.response.LoginResponse;
import com.ltalk.server.response.SignupResponse;
import lombok.Getter;

@Getter
public class ServerResponse {
    private ProtocolType protocolType;
    private boolean status;
    private ChatResponse chatResponse;
    private LoginResponse loginResponse;
    private SignupResponse signupResponse;

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

}
