package com.ltalk.server.response;

import com.ltalk.server.entity.Member;
import lombok.Getter;

import java.util.List;

@Getter
public class LoginResponse {
    private String msg;
    private Member member;
    private List<String> friends;

    public LoginResponse(String msg){
        this.msg = msg;
    }
    public LoginResponse(Member member, List<String> friendList, String msg) {
        this.member = member;
        this.msg = msg;
        this.friends = friendList;
    }
}
