package com.ltalk.server.request;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FriendRequest {
    String member;
    String friend;
    LocalDateTime RequestTime;
}
