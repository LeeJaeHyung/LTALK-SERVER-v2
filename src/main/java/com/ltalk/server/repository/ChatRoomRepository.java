package com.ltalk.server.repository;

import com.ltalk.server.entity.ChatRoom;
import com.ltalk.server.entity.Friend;
import com.ltalk.server.entity.ChatRoomMember;
import com.ltalk.server.enums.ChatRoomType;
import com.ltalk.server.request.ChatRoomCreatRequest;
import com.ltalk.server.util.JpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

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
        try {
            TypedQuery<ChatRoom> query = em.createQuery(
                    "SELECT cr FROM ChatRoom cr " +
                            "LEFT JOIN FETCH cr.members crm " +  // ChatRoom -> ChatRoomMember
                            "LEFT JOIN FETCH crm.member m " +   // ChatRoomMember -> Member
                            "WHERE cr.id = :id",
                    ChatRoom.class
            );
            query.setParameter("id", chatRoomId);
            chatRoom = query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JpaUtil.closeEntityManager(em);
        }
        return chatRoom;
    }


    public boolean existsPrivateChatRoomWithTwoMembers(String username1, String username2) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(cr) FROM ChatRoom cr " +
                            "JOIN cr.members crm " +
                            "JOIN crm.member m " +
                            "WHERE m.username IN (:username1, :username2) " +
                            "GROUP BY cr.chatRoomId " +
                            "HAVING COUNT(DISTINCT m.id) = 2 " +
                            "AND COUNT(crm) = 2",
                    Long.class
            );
            query.setParameter("username1", username1);
            query.setParameter("username2", username2);

            List<Long> result = query.getResultList();
            transaction.commit();

            return !result.isEmpty();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

}
