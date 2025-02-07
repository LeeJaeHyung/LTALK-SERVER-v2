package com.ltalk.server.entity;

import com.ltalk.server.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;

@Getter
@Setter
public class Client {

    String ip;
    Member member;
    UserRole userRole;
    AsynchronousSocketChannel socketChannel;

    public Client(AsynchronousSocketChannel socketChannel) throws IOException {
        member = null;
        userRole = UserRole.UNKNOWN_USER;
        this.socketChannel = socketChannel;
        this.ip = socketChannel.getRemoteAddress().toString();
    }

}
