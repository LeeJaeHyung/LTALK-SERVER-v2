package com.ltalk.server.controller;

import com.ltalk.server.entity.Data;
import com.ltalk.server.enums.ProtocolType;
import com.ltalk.server.handler.WriteHandler;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;

import static com.ltalk.server.service.DataService.gson;

public class VoiceServerController {

    public static AsynchronousSocketChannel voiceChannel = null;
    public static VoiceServerController voiceServerController = null;

    public VoiceServerController(AsynchronousSocketChannel voiceChannel) {
        VoiceServerController.voiceChannel = voiceChannel;
        voiceServerController = this;
    }

    public void creatVoiceServerController() {
        sendVoiceServer(new Data(ProtocolType.CRATE_VOICE_SERVER));
    }

    public void sendVoiceServer(Data data){
        String json = gson.toJson(data);
        byte[] jsonBytes = json.getBytes(StandardCharsets.UTF_8);
        int messageLength = jsonBytes.length;

        // 전송할 데이터 크기 출력
        System.out.println("[응답 전송 : " + json + "]");
        System.out.println("[응답 사이즈 : " + messageLength + "]");

        // 길이(4바이트) + JSON 데이터를 포함하는 ByteBuffer 생성
        ByteBuffer responseBuffer = ByteBuffer.allocate(4 + messageLength);
        responseBuffer.putInt(messageLength); // 4바이트 길이 정보 추가
        responseBuffer.put(jsonBytes);        // 본문 데이터 추가
        responseBuffer.flip(); // 버퍼를 읽을 수 있도록 준비

        // 클라이언트에게 응답 전송 (길이 포함된 데이터)
        voiceChannel.write(responseBuffer, responseBuffer, new WriteHandler(voiceChannel));
    }







}
