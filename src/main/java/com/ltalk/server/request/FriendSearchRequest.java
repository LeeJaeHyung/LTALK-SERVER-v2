package com.ltalk.server.request;

import lombok.Getter;

@Getter
public class FriendSearchRequest {
    private String friend_name;
    public FriendSearchRequest(String friend_name) {
        this.friend_name = friend_name;
    }
}
