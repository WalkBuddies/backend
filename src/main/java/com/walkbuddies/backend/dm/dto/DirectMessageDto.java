package com.walkbuddies.backend.dm.dto;

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
}
