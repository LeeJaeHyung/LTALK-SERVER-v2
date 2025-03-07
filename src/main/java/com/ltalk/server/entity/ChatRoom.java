package com.ltalk.server.entity;

import com.ltalk.server.enums.ChatRoomType;
import com.ltalk.server.request.ChatRoomCreatRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "chat_rooms")
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING) // Enum을 문자열로 저장
    @Column(nullable = false, length = 10)
    private ChatRoomType type; // 채팅방 타입 (PRIVATE / GROUP)

    @Column(nullable = false)
    private Integer participantCount = 0;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column()
    private LocalDateTime lastChattedAt;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true ,fetch = FetchType.LAZY)
    private Set<ChatRoomMember> members;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Chat> chats = new ArrayList<>();


    public ChatRoom(ChatRoomCreatRequest chatRoomCreatRequest) {
      this.type = chatRoomCreatRequest.getRoomType();
      this.name = chatRoomCreatRequest.getRoomName();
      this.members = new HashSet<>();
      this.participantCount = 0;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}