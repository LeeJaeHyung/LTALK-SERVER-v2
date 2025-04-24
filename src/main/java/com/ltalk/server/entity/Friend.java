package com.ltalk.server.entity;

import com.ltalk.server.enums.FriendStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "Friends")
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", nullable = false) // 현재 사용자의 ID
    private Member member;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "friend_id", nullable = false) // 친구의 ID
    private Member friend;

    @Setter
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private FriendStatus status = FriendStatus.PENDING;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Friend(Member member, Member friend, FriendStatus status) {
        this.member = member;
        this.friend = friend;
        this.status = status;
    }
}
