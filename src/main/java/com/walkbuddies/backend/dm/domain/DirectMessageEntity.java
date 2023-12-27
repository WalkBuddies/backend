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
    private ChatRoomEntity chatRoomId;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private MemberEntity senderId;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private MemberEntity recipientId;

    private String content;
    private String contentType;
    private LocalDateTime sendTime;

    public static DirectMessageDto entityToDto(DirectMessageEntity directMessageEntity) {
        return DirectMessageDto.builder()
                .messageId(directMessageEntity.getMessageId())
                .chatRoomId(directMessageEntity.getChatRoomId().getChatRoomId())
                .senderId(directMessageEntity.getSenderId().getMemberId())
                .recipientId(directMessageEntity.getRecipientId().getMemberId())
                .content(directMessageEntity.getContent())
                .contentType(directMessageEntity.getContentType())
                .sendTime(directMessageEntity.getSendTime())
                .build();
    }
}
