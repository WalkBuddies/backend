package com.walkbuddies.backend.club.repository;

import com.walkbuddies.backend.club.domain.ClubEntity;
import com.walkbuddies.backend.club.domain.ClubWaitingEntity;
import com.walkbuddies.backend.member.domain.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClubWaitingRepository extends JpaRepository<ClubWaitingEntity, Long> {

    Optional<ClubWaitingEntity> findByClubIdAndMemberId(ClubEntity clubEntity, MemberEntity memberEntity);

    List<ClubWaitingEntity> findByClubId(ClubEntity clubEntity);
}
