package com.walkbuddies.backend.dm.service;

import com.walkbuddies.backend.dm.dto.DirectMessageDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DirectMessageService {

    /**
     * 1 : 1 채팅 메서드
     * 채팅 내용을 DB에 저장
     * @param directMessageDto
     */
    void sendMessage(DirectMessageDto directMessageDto);

    /**
     * 해당하는 채팅방의 채팅 내역을 불러오는 메서드
     * @param chatRoomId
     * @return
     */
    List<DirectMessageDto> getMessage(Long chatRoomId);
}
