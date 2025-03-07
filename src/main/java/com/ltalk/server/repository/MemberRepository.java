package com.ltalk.server.repository;

import com.ltalk.server.entity.Chat;
import com.ltalk.server.entity.ChatRoom;
import com.ltalk.server.entity.Member;
import com.ltalk.server.util.JpaUtil;
import com.ltalk.server.entity.ChatRoomMember;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.HashSet;
import java.util.List;

public class MemberRepository {
    public boolean save(Member member) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(member);
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

    public void update(Member member) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(member);  // persist 대신 merge 사용
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            JpaUtil.closeEntityManager(em);
        }
    }

    public List<Member> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        List<Member> members = null;
        try {
            members = em.createQuery("SELECT m FROM Member m", Member.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JpaUtil.closeEntityManager(em);
        }
        return members;
    }

    public boolean usernameExists(String username) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Member> query = em.createQuery("SELECT m FROM Member m WHERE m.username = :username", Member.class);
            query.setParameter("username", username);
            return !query.getResultList().isEmpty();
        } finally {
            JpaUtil.closeEntityManager(em);
        }
    }

    public boolean emailExists(String email) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Member> query = em.createQuery("SELECT m FROM Member m WHERE m.email = :email", Member.class);
            query.setParameter("email", email);
            return !query.getResultList().isEmpty();
        } finally {
            JpaUtil.closeEntityManager(em);
        }
    }

    public Member findByUserName(String username) {
        EntityManager em = JpaUtil.getEntityManager();
        Member member = null;
        try{
            TypedQuery<Member> query = em.createQuery("SELECT m FROM Member m WHERE m.username = :username", Member.class);
            query.setParameter("username", username);
            member = query.getSingleResult();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JpaUtil.closeEntityManager(em);
        }
        return member;
    }

    public Member findMemberWithChatRooms(Long memberId) {
        EntityManager em = JpaUtil.getEntityManager();

        // 🚀 `ChatRoom`만 먼저 조회 (chats는 별도로 가져옴)
        String queryStr = "SELECT DISTINCT m FROM Member m " +
                "JOIN FETCH m.chatRooms crm " +
                "JOIN FETCH crm.chatRoom cr " +
                "LEFT JOIN FETCH cr.members " +
                "WHERE m.id = :memberId";

        TypedQuery<Member> query = em.createQuery(queryStr, Member.class);
        query.setParameter("memberId", memberId);
        Member member = query.getSingleResult();

        // 🚀 각 `ChatRoom`의 최근 10개 메시지만 가져오기
        for (ChatRoom chatRoom : member.getChatRooms().stream().map(ChatRoomMember::getChatRoom).toList()) {
            List<Chat> recentChats = getRecentChatsForChatRoom(chatRoom.getChatRoomId(), em);
            for(Chat chats : recentChats) {
                System.out.println(chats.getChatId());
            }
            chatRoom.setChats(recentChats); // 🚀 최근 10개의 Chat만 설정
        }

        return member;
    }

    /**
     * 특정 채팅방의 최근 10개 메시지를 가져오는 메서드
     */
    private List<Chat> getRecentChatsForChatRoom(Long chatRoomId, EntityManager em) {
        String chatQueryStr = "SELECT c FROM Chat c " +
                "WHERE c.chatRoom.chatRoomId = :chatRoomId " +
                "ORDER BY c.createdAt DESC";

        TypedQuery<Chat> chatQuery = em.createQuery(chatQueryStr, Chat.class);
        chatQuery.setParameter("chatRoomId", chatRoomId);
        chatQuery.setMaxResults(10); // 🚀 최신 10개 메시지만 가져오기

        return chatQuery.getResultList();
    }


    public Member findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        Member member = null;
        try{
            TypedQuery<Member> query = em.createQuery("SELECT m FROM Member m WHERE m.id = :id", Member.class);
            query.setParameter("id", id);
            member = query.getSingleResult();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JpaUtil.closeEntityManager(em);
        }
        return member;
    }
}
