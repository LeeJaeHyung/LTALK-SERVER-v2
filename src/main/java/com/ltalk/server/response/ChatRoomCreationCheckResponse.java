package com.ltalk.server.response;

import com.ltalk.server.dto.FriendDTO;

import java.util.List;

public class ChatRoomCreationCheckResponse {
    List<FriendDTO> friendDTOList;

    public ChatRoomCreationCheckResponse(List<FriendDTO> friendDTOList) {
        this.friendDTOList = friendDTOList;
    }

}
