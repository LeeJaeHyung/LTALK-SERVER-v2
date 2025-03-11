package com.ltalk.server.repository;

import com.ltalk.server.entity.ChatRoomMember;
import com.ltalk.server.util.JpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

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

    public void update(ChatRoomMember chatRoomMember) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(chatRoomMember);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            JpaUtil.closeEntityManager(em);
        }
    }

    public ChatRoomMember findByMemberIdAndChatRoomId(Long memberId, Long chatRoomId) {
        EntityManager em = JpaUtil.getEntityManager();
        ChatRoomMember chatRoomMember = null;
        try{
            TypedQuery<ChatRoomMember> query = em.createQuery(
                    "SELECT crm FROM ChatRoomMember crm WHERE crm.chatRoom.id = :chatRoomId AND crm.member.id = :memberId",
                    ChatRoomMember.class
            );
            query.setParameter("chatRoomId", chatRoomId);
            query.setParameter("memberId", memberId);
            chatRoomMember = query.getSingleResult();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JpaUtil.closeEntityManager(em);
        }
        return chatRoomMember;
    }


    public List<ChatRoomMember> findByChatRoomIdWithMember(Long chatRoomId) {
        EntityManager em = JpaUtil.getEntityManager();
        List<ChatRoomMember> chatRoomMembers = null;

        try {
            TypedQuery<ChatRoomMember> query = em.createQuery(
                    "SELECT crm FROM ChatRoomMember crm " +
                            "JOIN FETCH crm.member " +  // 멤버 정보를 함께 로딩
                            "WHERE crm.chatRoom.id = :chatRoomId",
                    ChatRoomMember.class
            );
            query.setParameter("chatRoomId", chatRoomId);

            chatRoomMembers = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JpaUtil.closeEntityManager(em);
        }

        return chatRoomMembers;
    }
}
