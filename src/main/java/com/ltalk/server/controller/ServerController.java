package com.ltalk.server.controller;

import com.ltalk.server.entity.Client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

public class ServerController {
    private static final int PORT = 7623;
    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    private static volatile boolean isRunning = false;// true == 서버 실행중 , false == 서버실행 전

    private static AsynchronousChannelGroup channelGroup;
    private static AsynchronousServerSocketChannel serverSocketChannel;
    public static ConcurrentHashMap<String, Client> clients = new ConcurrentHashMap();


    public static void startServer() throws IOException {
        if (isRunning) {
            System.out.println("서버가 이미 실행 중입니다.");
            return;
        }

        isRunning = true; // 서버 시작 플래그 설정

        channelGroup = AsynchronousChannelGroup.withFixedThreadPool(THREAD_POOL_SIZE, Executors.defaultThreadFactory());
        serverSocketChannel = AsynchronousServerSocketChannel.open(channelGroup);
        serverSocketChannel.bind(new InetSocketAddress("localhost", PORT));



    }
}
