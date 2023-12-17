package com.walkbuddies.backend.club.repository;

import com.walkbuddies.backend.club.domain.ClubBoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClubBoardRepository extends JpaRepository<ClubBoardEntity, Long> {
    Optional<ClubBoardEntity> findByClubBoardId(long boardIdx);
}
