package com.ltalk.server.repository;

import com.ltalk.server.entity.ChatRoom;
import com.ltalk.server.entity.Friend;
import com.ltalk.server.entity.Member;
import com.ltalk.server.util.JpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

public class ChatRoomRepository {

    public boolean save(ChatRoom chatRoom) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(chatRoom);
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

    public void update(ChatRoom chatRoom) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(chatRoom);  // persist 대신 merge 사용
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            JpaUtil.closeEntityManager(em);
        }
    }

    public ChatRoom roomFindById(Long chatRoomId) {
        EntityManager em = JpaUtil.getEntityManager();
        ChatRoom chatRoom = null;
        try{
            TypedQuery<ChatRoom> query = em.createQuery("SELECT cr FROM ChatRoom cr WHERE cr.id = :id", ChatRoom.class);
            query.setParameter("id", chatRoomId);
            chatRoom = query.getSingleResult();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JpaUtil.closeEntityManager(em);
        }
        return chatRoom;
    }
}
