package com.walkbuddies.backend.repository.clubservice;

import com.walkbuddies.backend.domain.clubservice.ClubEntity;
import com.walkbuddies.backend.domain.clubservice.ClubWaitingEntity;
import com.walkbuddies.backend.domain.memberservice.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClubWaitingRepository extends JpaRepository<ClubWaitingEntity, Long> {

    List<ClubWaitingEntity> findByClubId(Optional<ClubEntity> optionalClub);

    Optional<ClubWaitingEntity> findByClubIdAndMemberId(ClubEntity clubEntity, MemberEntity memberEntity);
}
