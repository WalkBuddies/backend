package com.walkbuddies.backend.dm.service;

import com.walkbuddies.backend.dm.dto.DirectMessageDto;
import org.springframework.stereotype.Service;

@Service
public interface DirectMessageService {

    void sendMessage(DirectMessageDto directMessageDto);
}
