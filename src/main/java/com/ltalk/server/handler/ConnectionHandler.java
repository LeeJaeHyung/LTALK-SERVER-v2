package com.ltalk.server.handler;

import com.ltalk.server.Main;
import com.ltalk.server.controller.ServerController;
import com.ltalk.server.entity.Client;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ConnectionHandler implements CompletionHandler <AsynchronousSocketChannel, Void> {
    @Override
    public void completed(AsynchronousSocketChannel socketChannel, Void attachment) {
        try{
            Main.control.addText("[ 연결 => "+socketChannel.getRemoteAddress()+"]");
            Client client = new Client(socketChannel);
            ServerController.clients.put(socketChannel.getRemoteAddress().toString(), client);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void failed(Throwable exc, Void attachment) {

    }
}
