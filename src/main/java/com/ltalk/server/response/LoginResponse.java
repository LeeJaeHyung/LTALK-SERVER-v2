package com.ltalk.server.response;

import com.ltalk.entity.Member;
import lombok.Getter;

@Getter
public class LoginResponse {
    private String msg;
    private Member member;

    public LoginResponse(String msg){
        this.msg = msg;
    }
    public LoginResponse(Member member, String msg) {
        this.member = member;
        this.msg = msg;
    }
}
