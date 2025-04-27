package com.ltalk.server.repository;

import com.ltalk.server.entity.Friend;
import com.ltalk.server.entity.Member;
import com.ltalk.server.enums.FriendStatus;
import com.ltalk.server.util.JpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.List;

public class FriendRepository {

    public void updateFriend(Friend friend) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.merge(friend);  // ✅ 그냥 merge만!
            tx.commit();
            System.out.println("친구 상태 업데이트 완료!");
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            JpaUtil.closeEntityManager(em);
        }
    }

    public boolean save(Friend friend) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(friend);
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

    public boolean checkRequest(String memberName, String friendName) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            String friendQuery =
                    "SELECT f FROM Friend f " +
                            "WHERE ((f.member.username = :memberName AND f.friend.username = :friendName) " +
                            "    OR (f.member.username = :friendName AND f.friend.username = :memberName)) " +
                            "AND (f.status = :accepted OR f.status = :pending)";

            List<Friend> friendList = em.createQuery(friendQuery, Friend.class)
                    .setParameter("memberName", memberName)
                    .setParameter("friendName", friendName)
                    .setParameter("accepted", FriendStatus.ACCEPTED)
                    .setParameter("pending", FriendStatus.PENDING)   // ✅ 여기!
                    .getResultList();

            if (friendList.isEmpty()) return false;

            Friend myFriend = null;
            int acceptCount = 0;
            for (Friend friend : friendList) {
                if (friend.getMember().getUsername().equals(memberName)) {  // == → .equals()로 수정
                    myFriend = friend;
                }
                if (friend.getStatus() == FriendStatus.ACCEPTED) {
                    acceptCount++;
                }
            }

            if (acceptCount == 1 && myFriend != null && myFriend.getStatus() == FriendStatus.PENDING) {
                myFriend.setStatus(FriendStatus.ACCEPTED);
                updateFriend(myFriend);
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return true;
        } finally {
            JpaUtil.closeEntityManager(em);
        }
    }

    public List<Friend> findMyFriendsWithoutPrivateChatRoom(Long memberId) {
        EntityManager em = JpaUtil.getEntityManager();  // 너가 쓰는 EntityManager 팩토리
        EntityTransaction tx = em.getTransaction();

        List<Friend> result = new ArrayList<>();

        try {
            tx.begin();

            String jpql = "SELECT f " +
                    "FROM Friend f " +
                    "WHERE f.member.id = :memberId " +
                    "AND f.status = 'ACCEPTED' " +
                    "AND EXISTS ( " +
                    "   SELECT 1 " +
                    "   FROM Friend f2 " +
                    "   WHERE f2.member = f.friend " +   // 상대가 나를 친구로 등록
                    "   AND f2.friend.id = :memberId " +
                    "   AND f2.status = 'ACCEPTED' " +
                    ") " +
                    "AND NOT EXISTS ( " +
                    "   SELECT crm1 " +
                    "   FROM ChatRoomMember crm1 " +
                    "   JOIN crm1.chatRoom cr " +
                    "   JOIN cr.members crm2 " +
                    "   WHERE crm1.member.id = :memberId " +
                    "   AND crm2.member = f.friend " +
                    "   AND cr.type = 'PRIVATE' " +
                    "   AND SIZE(cr.members) = 2 " +
                    ")";


            result = em.createQuery(jpql, Friend.class)
                    .setParameter("memberId", memberId)
                    .getResultList();

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        return result;
    }



}
