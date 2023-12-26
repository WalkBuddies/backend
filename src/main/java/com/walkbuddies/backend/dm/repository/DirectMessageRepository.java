package com.walkbuddies.backend.dm.repository;

import com.walkbuddies.backend.dm.domain.DirectMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectMessageRepository extends JpaRepository<DirectMessageEntity, Long> {
}
