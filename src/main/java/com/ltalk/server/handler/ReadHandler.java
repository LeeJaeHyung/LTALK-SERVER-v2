package com.ltalk.server.handler;

import com.ltalk.server.entity.Data;
import com.ltalk.server.service.DataService;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

import static com.ltalk.server.service.DataService.gson;

public class ReadHandler implements CompletionHandler<Integer, ByteBuffer> {
    private final AsynchronousSocketChannel socketChannel;
    private DataService dataService;

    public ReadHandler(AsynchronousSocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }


    @Override
    public void completed(Integer bytesRead, ByteBuffer buffer) {
        if (bytesRead == -1) {  // 클라이언트 연결 종료 확인
            closeConnection();
            return;
        }
        buffer.flip();
        String receivedJson = StandardCharsets.UTF_8.decode(buffer).toString();
        System.out.println(receivedJson);
        Data data = gson.fromJson(receivedJson, Data.class);
        try {
            dataService = new DataService(socketChannel,data);// 데이터 서비스를 생성과 동시에 interpret()동작
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ByteBuffer newBuffer = ByteBuffer.allocate(1024);
        socketChannel.read(newBuffer, newBuffer, this);
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {

    }

    private void closeConnection() {
        try {
            System.out.println("[서버] 클라이언트 연결 종료: " + socketChannel.getRemoteAddress());
            socketChannel.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
