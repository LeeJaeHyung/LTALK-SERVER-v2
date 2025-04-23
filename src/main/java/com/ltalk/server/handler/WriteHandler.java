package com.ltalk.server.handler;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class WriteHandler implements CompletionHandler<Integer, ByteBuffer> {
    private final AsynchronousSocketChannel socketChannel;
    private ByteBuffer buffer;

    public WriteHandler(AsynchronousSocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        if (buffer.hasRemaining()) {
            socketChannel.write(buffer, buffer, this); // 남은 데이터가 있으면 계속 전송
        } else {
            System.out.println("[서버] 응답 전송 완료");
            buffer.clear(); // 메모리 해제
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer buffer) {
        System.err.println("[서버] 데이터 전송 실패: " + exc.getMessage());
    }
}
