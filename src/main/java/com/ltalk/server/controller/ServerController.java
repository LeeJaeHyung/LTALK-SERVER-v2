package com.ltalk.server.controller;

import com.ltalk.server.entity.Client;
import com.ltalk.server.handler.ConnectionHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.ltalk.server.Main.viewController;

public class ServerController {
    private static final int PORT = 7623;
    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    public static volatile boolean isRunning = false; // 서버 실행 상태 플래그

    private static AsynchronousChannelGroup channelGroup;
    public static AsynchronousServerSocketChannel serverSocketChannel;
    public static ConcurrentHashMap<String, Client> clients = new ConcurrentHashMap<>();

    /**
     * 서버를 시작하는 메서드
     */
    public static void startServer() throws IOException {
        if (isRunning) {
            viewController.addText("서버가 이미 실행 중입니다.");
            return;
        }

        isRunning = true;
        viewController.clearText();
        viewController.addText("서버 시작 중...");

        // AsynchronousChannelGroup 생성
        channelGroup = AsynchronousChannelGroup.withFixedThreadPool(THREAD_POOL_SIZE, Executors.defaultThreadFactory());

        // AsynchronousServerSocketChannel 생성 및 바인딩
        serverSocketChannel = AsynchronousServerSocketChannel.open(channelGroup);
        serverSocketChannel.bind(new InetSocketAddress("localhost", PORT));

        viewController.addText("서버가 포트 " + PORT + "에서 실행 중...");

        // 클라이언트 연결 대기
        serverSocketChannel.accept(null, new ConnectionHandler());
    }

    /**
     * 서버를 안전하게 종료하는 메서드
     */
    public static void stopServer() {
        viewController.addText("서버 종료 요청됨.");
        if (!isRunning) {
            viewController.addText("서버가 실행 중이 아닙니다.");
            return;
        }

        viewController.addText("서버 종료 중...");
        isRunning = false;
        try {
            // **1. 모든 클라이언트 연결 닫기**
//            clients.forEach((key, client) -> {
//                try {
//                    client.close(); // Client 클래스에서 close() 구현 필요
//                } catch (IOException e) {
//                    System.err.println("클라이언트 연결 종료 중 오류 발생: " + e.getMessage());
//                }
//            });

            // 클라이언트 목록 정리
            clients.clear();

            // **2. 서버 소켓 채널 닫기**
            if (serverSocketChannel != null && serverSocketChannel.isOpen()) {
                serverSocketChannel.close();
                viewController.addText("serverSocketChannel is closed.");
            }

            // **3. 채널 그룹 종료 처리**
            try {
                // 서버 종료 요청
                channelGroup.shutdown();
                viewController.addText("channelGroup is shut down.");

                // 5초 동안 대기하며 현재 진행 중인 작업이 종료되길 기다림
                if (!channelGroup.awaitTermination(5, TimeUnit.SECONDS)) {
                    viewController.addText("5초 내 종료되지 않아 강제 종료 시도...");
                    channelGroup.shutdownNow(); // 강제 종료

                    // 강제 종료 후 다시 확인
                    if (!channelGroup.awaitTermination(3, TimeUnit.SECONDS)) {
                        viewController.addText("channelGroup이 여전히 닫히지 않음.");
                    }
                }

                viewController.addText("channelGroup 정상 종료됨.");
            } catch (InterruptedException e) {
                viewController.addText("서버 종료 대기 중 인터럽트 발생: " + e.getMessage());
                Thread.currentThread().interrupt(); // 현재 스레드의 인터럽트 상태를 유지
            } catch (Exception e) {
                viewController.addText("서버 종료 중 예외 발생: " + e.getMessage());
            }
            viewController.clearText();
            viewController.addText("서버가 정상적으로 종료되었습니다.");
            System.out.println("서버가 정상적으로 종료되었습니다.");
        } catch (IOException e) {
            viewController.addText("서버 종료 실패: " + e.getMessage());
        }
    }
}
