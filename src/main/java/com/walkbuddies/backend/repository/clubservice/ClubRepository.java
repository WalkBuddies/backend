package com.walkbuddies.backend.repository.clubservice;

import com.walkbuddies.backend.domain.clubservice.ClubEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClubRepository extends JpaRepository<ClubEntity, Long> {
    Optional<ClubEntity> findByClubName(String clubName);

    List<ClubEntity> findByClubNameContaining(String clubName);

    Optional<ClubEntity> findByClubId(Long clubId);

}
