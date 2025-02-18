package com.ltalk.server.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ltalk.server.entity.Data;
import com.ltalk.server.entity.Member;
import com.ltalk.server.entity.ServerResponse;
import com.ltalk.server.handler.WriteHandler;
import com.ltalk.server.util.LocalDateTimeAdapter;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Getter
@Setter
public class DataService {

    private Data data;
    private final AsynchronousSocketChannel socketChannel;
    private MemberService memberService;
    public static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

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
        send(loginResponse);
    }
    private void chat(){

    }
    private void signup() throws NoSuchAlgorithmException, IOException {
        memberService = new MemberService(socketChannel);
        ServerResponse signupResponse = memberService.signup(new Member(data.getSignupRequest()));
        send(signupResponse);
    }

    private void send(ServerResponse response){
        String json = gson.toJson(response);
        System.out.println("[응답 전송 : "+json+"]");
        ByteBuffer responseBuffer = ByteBuffer.wrap(gson.toJson(response).getBytes(StandardCharsets.UTF_8));
        // 클라이언트에게 응답 전송
        socketChannel.write(responseBuffer, responseBuffer, new WriteHandler(socketChannel));
    }



}
