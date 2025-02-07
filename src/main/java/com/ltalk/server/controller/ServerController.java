package com.ltalk.server.controller;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.ltalk.server.Main.control;

public class ServerController {
    private static final int PORT = 7623;
    private static final int THREAD_POOL_SIZE = 10;

    private static volatile boolean isRunning = false;// true == 서버 실행중 , false == 서버실행 전
    private static Selector selector;
    private static ServerSocketChannel serverChannel;
    private static ExecutorService threadPool;


    public static void startServer() throws IOException {
        if (isRunning) {
            System.out.println("서버가 이미 실행 중입니다.");
            return;
        }

        isRunning = true; // 서버 시작 플래그 설정
        serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(PORT));
        serverChannel.configureBlocking(false);

        // Selector 생성
        selector = Selector.open();
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 제한된 스레드 풀 생성 (고정 개수)
        threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        control.addText("서버가 포트 " + PORT + "에서 실행 중...");

        new Thread(() -> {
            try {
                runEventLoop();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void runEventLoop() throws IOException { // 서버 실행시 반복 루프
        while (isRunning) { // 서버가 실행 중일 때만 반복
            selector.select(); // 이벤트가 발생할 때까지 대기

            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                keyIterator.remove();

                if (key.isAcceptable()) {
                    acceptConnection();// 서버와 클라이언트 연결
                } else if (key.isReadable()) { // 서버에대한 연결 처리
                    threadPool.submit(() -> handleClientRequest(key));
                }
            }
        }
        shutdownServer(); // 종료 루프를 빠져나오면 서버 종료
    }

    private static void acceptConnection() throws IOException {
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);
        control.addText("새로운 클라이언트 연결됨: " + clientChannel.getRemoteAddress());
    }

    private static void handleClientRequest(SelectionKey key) {
        try {
            SocketChannel clientChannel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(256);
            int bytesRead = clientChannel.read(buffer);

            if (bytesRead == -1) {
                key.cancel();  // Selector에서 해당 키 제거
                clientChannel.close();
                control.addText("클라이언트 연결 종료");
                return;
            }

            buffer.flip();
            String message = new String(buffer.array(), 0, buffer.limit()).trim();
            control.addText("클라이언트 메시지: " + message);

            // 클라이언트에게 응답 보내기
            buffer.clear();
            buffer.put(("서버 응답: " + message).getBytes());
            buffer.flip();
            clientChannel.write(buffer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void shutdownServer() {
        if (!isRunning) {
            control.addText("서버가 실행 중이 아닙니다.");
            return;
        }

        isRunning = false;
        control.addText("서버 종료 중...");

        try {
            // 스레드 풀 종료
            if (threadPool != null) {
                threadPool.shutdown();
                control.addText("스레드 풀 종료 완료");
            }

            // Selector 닫기
            if (selector != null && selector.isOpen()) {
                selector.close();
                control.addText("Selector 종료 완료");
            }

            // ServerSocketChannel 닫기
            if (serverChannel != null && serverChannel.isOpen()) {
                serverChannel.close();
                control.addText("ServerSocketChannel 종료 완료");
            }

            control.addText("서버가 정상적으로 종료되었습니다.");
        } catch (IOException e) {
            control.addText("서버 종료 중 오류 발생: " + e.getMessage());
        }
    }

}
