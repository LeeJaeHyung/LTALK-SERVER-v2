package com.ltalk.server.service;

import com.ltalk.server.entity.Friend;
import com.ltalk.server.repository.FriendRepository;

public class FriendService {
    FriendRepository friendRepository = new FriendRepository();

    public void addFriend(Friend friend1, Friend friend2) {
        friendRepository.save(friend1);
        friendRepository.save(friend2);
    }

    public boolean checkRequest(String memberName, String friendName) {
        return friendRepository.checkRequest(memberName,friendName);
    }
}
