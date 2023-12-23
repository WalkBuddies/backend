package com.walkbuddies.backend.club.repository;

import com.walkbuddies.backend.club.domain.ClubEntity;
import com.walkbuddies.backend.member.domain.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClubRepository extends JpaRepository<ClubEntity, Long> {
    Optional<ClubEntity> findByClubName(String clubName);

    List<ClubEntity> findByClubNameContaining(String clubName);

    Optional<ClubEntity> findByClubId(Long clubId);

    Optional<ClubEntity> findByClubIdAndOwnerId(Long clubId, MemberEntity memberEntity);
}
