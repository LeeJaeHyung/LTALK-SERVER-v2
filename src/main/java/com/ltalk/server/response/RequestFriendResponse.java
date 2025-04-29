package com.ltalk.server.response;

import com.ltalk.server.dto.FriendDTO;

public class RequestFriendResponse {
    FriendDTO friendDTO;
    public RequestFriendResponse(FriendDTO friendDTO) {
        this.friendDTO = friendDTO;
    }
}
