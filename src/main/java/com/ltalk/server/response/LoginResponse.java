package com.ltalk.server.response;

import com.ltalk.server.dto.ChatRoomDTO;
import com.ltalk.server.dto.FriendDTO;
import com.ltalk.server.dto.MemberDTO;
import com.ltalk.server.entity.Member;
import lombok.Getter;

import java.util.List;

@Getter
public class LoginResponse {
    private String msg;
    private MemberDTO member;


    public LoginResponse(String msg){
        this.msg = msg;
    }
    public LoginResponse(MemberDTO member, String msg) {
        this.member = member;
        this.msg = msg;
    }
}
