package com.walkbuddies.backend.dm.repository;

import com.walkbuddies.backend.dm.domain.ChatRoomEntity;
import com.walkbuddies.backend.dm.domain.DirectMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DirectMessageRepository extends JpaRepository<DirectMessageEntity, Long> {
    Optional<List<DirectMessageEntity>> findByChatRoomId(ChatRoomEntity chatRoom);
}
