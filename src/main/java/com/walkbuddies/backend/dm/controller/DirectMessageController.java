package com.walkbuddies.backend.dm.controller;

import com.walkbuddies.backend.common.response.ListResponse;
import com.walkbuddies.backend.dm.dto.DirectMessageDto;
import com.walkbuddies.backend.dm.service.DirectMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DirectMessageController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final DirectMessageService directMessageService;

    @MessageMapping("/chat/sendMessage")
    public void sendMessage(@Payload DirectMessageDto directMessageDto) {
        // 웹 소켓을 통해 메시지를 수신하고 다시 수신자에게 해당 메시지를 전송
        simpMessagingTemplate.convertAndSendToUser(
                directMessageDto.getRecipientId().toString(),
                "/topic/private",
                directMessageDto);

        // 메시지를 DB에 저장하는 서비스 호출
        directMessageService.sendMessage(directMessageDto);
    }

    @GetMapping("/chat/getMessage")
    public ListResponse getMessage(@RequestParam(name = "chatRoomId") Long chatRoomId) {

        ListResponse listResponse = new ListResponse(HttpStatus.OK.value(), "채팅 내용을 불러왔습니다.",
                directMessageService.getMessage(chatRoomId));
        return listResponse;
    }

}
