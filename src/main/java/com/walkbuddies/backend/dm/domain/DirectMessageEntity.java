package com.walkbuddies.backend.dm.domain;

import com.walkbuddies.backend.dm.dto.DirectMessageDto;
import com.walkbuddies.backend.member.domain.MemberEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "direct_message")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DirectMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoomEntity chatRoom;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private MemberEntity sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private MemberEntity recipient;

    private String content;
    private String contentType;
    private LocalDateTime sendTime;
}
