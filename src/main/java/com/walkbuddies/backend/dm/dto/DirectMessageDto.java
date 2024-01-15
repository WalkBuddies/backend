package com.walkbuddies.backend.dm.dto;

import com.walkbuddies.backend.dm.domain.DirectMessageEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DirectMessageDto {

    private Long messageId;
    private Long chatRoomId;
    private Long senderId;
    private Long recipientId;
    private String content;
    private String contentType;
    private LocalDateTime sendTime;

    public static DirectMessageDto of(DirectMessageEntity directMessageEntity) {
        return DirectMessageDto.builder()
                .messageId(directMessageEntity.getMessageId())
                .chatRoomId(directMessageEntity.getChatRoom().getChatRoomId())
                .senderId(directMessageEntity.getSender().getMemberId())
                .recipientId(directMessageEntity.getRecipient().getMemberId())
                .content(directMessageEntity.getContent())
                .contentType(directMessageEntity.getContentType())
                .sendTime(directMessageEntity.getSendTime())
                .build();
    }
}
