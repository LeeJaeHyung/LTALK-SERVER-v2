package com.ltalk.server.controller;

import com.ltalk.server.entity.Data;
import com.ltalk.server.enums.ProtocolType;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.ltalk.server.service.DataService.gson;

public class VoiceServerController {

    public static AsynchronousSocketChannel voiceChannel = null;
    public static VoiceServerController voiceServerController = null;
    public static boolean voiceServerIsRunning = false;
    public static String voiceServerIp = null;

    private static final BlockingQueue<ByteBuffer> writeQueue = new LinkedBlockingQueue<>();
    private static volatile boolean writing = false;

    public VoiceServerController(AsynchronousSocketChannel voiceChannel) throws IOException {
        VoiceServerController.voiceChannel = voiceChannel;
        InetSocketAddress remoteAddress = (InetSocketAddress) voiceChannel.getRemoteAddress();
        voiceServerIp = remoteAddress.getAddress().getHostAddress(); // "127.0.0.1"
        voiceServerController = this;
        voiceServerIsRunning = true;
    }

    public void creatVoiceServerController() {
        sendVoiceServer(new Data(ProtocolType.CRATE_VOICE_SERVER));
    }

    public static void sendVoiceServer(Data data) {
        String json = gson.toJson(data);
        byte[] jsonBytes = json.getBytes(StandardCharsets.UTF_8);
        int messageLength = jsonBytes.length;

        System.out.println("[응답 전송 : " + json + "]");
        System.out.println("[응답 사이즈 : " + messageLength + "]");

        ByteBuffer buffer = ByteBuffer.allocate(4 + messageLength);
        buffer.putInt(messageLength);
        buffer.put(jsonBytes);
        buffer.flip();

        enqueueWrite(buffer);
    }

    private static void enqueueWrite(ByteBuffer buffer) {
        writeQueue.add(buffer);
        tryWrite();
    }

    private static synchronized void tryWrite() {
        if (writing) return;

        ByteBuffer buffer = writeQueue.poll();
        if (buffer == null) return;

        writing = true;
        voiceChannel.write(buffer, buffer, new CompletionHandler<>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                writing = false;
                tryWrite();
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                writing = false;
                exc.printStackTrace();
            }
        });
    }
}
