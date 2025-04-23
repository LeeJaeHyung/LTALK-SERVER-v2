package com.ltalk.server.handler;

import com.ltalk.server.Main;
import com.ltalk.server.controller.ServerController;
import com.ltalk.server.entity.Client;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import static com.ltalk.server.Main.viewController;
import static com.ltalk.server.controller.ServerController.clients;
import static com.ltalk.server.controller.ServerController.serverSocketChannel;

public class ConnectionHandler implements CompletionHandler<AsynchronousSocketChannel, Void> {

    @Override
    public void completed(AsynchronousSocketChannel socketChannel, Void attachment) {
        if (!ServerController.isRunning) {
            viewController.addText("서버가 종료된 상태이므로 클라이언트 연결을 받지 않습니다.");
            return;
        }

        try {
            String clientAddress = socketChannel.getRemoteAddress().toString();
            Main.viewController.addText("[ 연결 => " + clientAddress + " ]");

            // 새로운 클라이언트 객체 생성 및 저장
            Client client = new Client(socketChannel);
            ServerController.clients.put(clientAddress, client);
            System.out.println("key : " + clientAddress);
            viewController.addText("참여 인원 : "+clients.mappingCount());


        } catch (IOException e) {
            viewController.addText("클라이언트 주소를 가져오는 도중 오류 발생: " + e.getMessage());
        } catch (Exception e) {
            viewController.addText("클라이언트 연결 처리 중 예외 발생: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 서버가 실행 중일 때만 accept() 호출
            if (ServerController.isRunning) {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                socketChannel.read(buffer, buffer, new ReadHandler(socketChannel));// 해당 클라이언트의 데이터 수신 시작
                serverSocketChannel.accept(null, this);
            } else {
                viewController.addText("서버가 종료 상태이므로 새로운 클라이언트 요청을 받지 않습니다.");
            }
        }
    }

    @Override
    public void failed(Throwable exc, Void attachment) {
        if (!ServerController.isRunning) {
            viewController.addText("서버가 종료된 상태에서 클라이언트 연결 실패 발생. accept() 호출 중단.");
            return;
        }

        viewController.addText("클라이언트 연결 실패: " + exc.getMessage());

        // 서버가 실행 중일 때만 accept() 호출
        if (ServerController.isRunning) {
            serverSocketChannel.accept(null, this);
        } else {
            viewController.addText("서버가 종료 상태이므로 새로운 클라이언트 요청을 받지 않습니다.");
        }
    }
}
