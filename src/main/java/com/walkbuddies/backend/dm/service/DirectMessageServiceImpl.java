package com.walkbuddies.backend.dm.service;

import com.walkbuddies.backend.dm.domain.ChatRoomEntity;
import com.walkbuddies.backend.dm.domain.DirectMessageEntity;
import com.walkbuddies.backend.dm.dto.DirectMessageDto;
import com.walkbuddies.backend.dm.repository.ChatRoomRepository;
import com.walkbuddies.backend.dm.repository.DirectMessageRepository;
import com.walkbuddies.backend.exception.impl.NotFoundChatRoomException;
import com.walkbuddies.backend.exception.impl.NotFoundMemberException;
import com.walkbuddies.backend.member.domain.MemberEntity;
import com.walkbuddies.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DirectMessageServiceImpl implements DirectMessageService {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MemberRepository memberRepository;
    private final DirectMessageRepository directMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    /**
     * 1 : 1 채팅 메서드
     * 채팅 내용을 DB에 저장
     * @param directMessageDto
     */
    @Transactional
    @Override
    public void sendMessage(DirectMessageDto directMessageDto) {

        MemberEntity sender = getMemberEntity(directMessageDto.getSenderId());
        MemberEntity recipient = getMemberEntity(directMessageDto.getRecipientId());

        MemberEntity firstMember = (sender.getMemberId() < recipient.getMemberId()) ? sender : recipient;
        MemberEntity secondMember = (sender.getMemberId() < recipient.getMemberId()) ? recipient : sender;

        // 메시지를 전송할 대상의 WebSocket 주소
        String destination = "/user/" + directMessageDto.getRecipientId() + "/topic/private";

        // WebSocket 메시지 전송
        simpMessagingTemplate.convertAndSend(destination, directMessageDto);

        // ChatRoom DB 저장
        Optional<ChatRoomEntity> optionalChatRoom = chatRoomRepository.findBySenderIdAndRecipientId(firstMember, secondMember);
        ChatRoomEntity chatRoom = optionalChatRoom.orElseGet(() -> createChatRoom(sender, recipient));

        // DirectMessage DB 저장
        DirectMessageEntity directMessageEntity = DirectMessageEntity.builder()
                .chatRoomId(chatRoom)
                .senderId(sender)
                .recipientId(recipient)
                .content(directMessageDto.getContent())
                .contentType(directMessageDto.getContentType())
                .sendTime(LocalDateTime.now())
                .build();
        directMessageRepository.save(directMessageEntity);
    }

    private static final String REDIS_KEY_PREFIX = "chat:roomId:";
    /**
     * 해당하는 채팅방의 채팅 내역을 불러오는 메서드
     * @param chatRoomId
     * @return
     */
    @Override
    public List<DirectMessageDto> getMessage(Long chatRoomId) {

        String redisKey = REDIS_KEY_PREFIX + chatRoomId;
        List<DirectMessageDto> directMessageDtos = new ArrayList<>();
        Optional<ChatRoomEntity> optionalChatRoom = chatRoomRepository.findByChatRoomId(chatRoomId);
            if (optionalChatRoom.isEmpty()) {
                throw new NotFoundChatRoomException();
            }

            ChatRoomEntity chatRoom = optionalChatRoom.get();
            Optional<List<DirectMessageEntity>> directMessageEntities = directMessageRepository.findByChatRoomId(chatRoom);

            for (DirectMessageEntity directMessageEntity : directMessageEntities.get()) {
                directMessageDtos.add(DirectMessageEntity.entityToDto(directMessageEntity));
            }

        return directMessageDtos;
    }


    private MemberEntity getMemberEntity(Long memberId) {
        return memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NotFoundMemberException());
    }

    private ChatRoomEntity createChatRoom(MemberEntity sender, MemberEntity recipient) {
        ChatRoomEntity chatRoomEntity = ChatRoomEntity.builder()
                .senderId(sender)
                .recipientId(recipient)
                .build();
        return chatRoomRepository.save(chatRoomEntity);
    }
}
