package com.walkbuddies.backend.dm.repository;

import com.walkbuddies.backend.dm.domain.ChatRoomEntity;
import com.walkbuddies.backend.member.domain.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> {
    Optional<ChatRoomEntity> findBySenderIdAndRecipientId(MemberEntity sender, MemberEntity recipient);
}
