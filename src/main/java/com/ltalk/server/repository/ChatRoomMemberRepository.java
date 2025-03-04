package com.ltalk.server.repository;

import com.ltalk.server.entity.ChatRoom;
import com.ltalk.server.entity.ChatRoomMember;
import com.ltalk.server.util.JpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class ChatRoomMemberRepository {

    public boolean save(ChatRoomMember chatRoomMember) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(chatRoomMember);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            JpaUtil.closeEntityManager(em);
        }
        return true;
    }
}
