package com.ltalk.server.response;

import com.ltalk.server.dto.FriendDTO;
import com.ltalk.server.entity.Member;

import java.util.ArrayList;
import java.util.List;

public class FriendSearchResponse {
    List<FriendDTO> friendDTOList;

    public FriendSearchResponse(List<Member> friendList) {
        friendDTOList = new ArrayList<>();
        for(Member member : friendList) {
            friendDTOList.add(new FriendDTO(member));
        }
    }
}
