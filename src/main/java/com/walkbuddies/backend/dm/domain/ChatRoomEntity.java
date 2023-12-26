package com.walkbuddies.backend.dm.domain;

import com.walkbuddies.backend.member.domain.MemberEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_room")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private MemberEntity senderId;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private MemberEntity recipientId;

}
