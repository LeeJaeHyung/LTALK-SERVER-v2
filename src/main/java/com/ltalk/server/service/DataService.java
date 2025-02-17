package com.ltalk.server.service;

import com.ltalk.server.entity.Data;
import com.ltalk.server.entity.Member;
import com.ltalk.server.entity.ServerResponse;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;
import java.security.NoSuchAlgorithmException;

@Getter
@Setter
public class DataService {

    private Data data;
    private final AsynchronousSocketChannel socketChannel;
    private MemberService memberService;

    public DataService(AsynchronousSocketChannel socketChannel, Data data) throws NoSuchAlgorithmException, IOException {
       this.socketChannel = socketChannel;
       this.data = data;
       interpret();
    }

    private void interpret() throws NoSuchAlgorithmException, IOException {
        switch(data.getProtocolType()){
            case LOGIN -> login();
            case CHAT -> chat();
            case SIGNUP -> signup();
        }
    }

    private void login() throws NoSuchAlgorithmException, IOException {
        memberService = new MemberService(socketChannel);
        ServerResponse loginResponse = memberService.login(new Member(data.getLoginRequest()));

    }
    private void chat(){

    }
    private void signup(){

    }

    private void send(){

    }



}
