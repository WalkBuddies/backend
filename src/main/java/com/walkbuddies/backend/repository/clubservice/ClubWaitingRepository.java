package com.walkbuddies.backend.repository.clubservice;

import com.walkbuddies.backend.domain.clubservice.ClubWaitingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubWaitingRepository extends JpaRepository<ClubWaitingEntity, Long> {
}
