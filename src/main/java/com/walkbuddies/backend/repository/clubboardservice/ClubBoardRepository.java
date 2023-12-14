package com.walkbuddies.backend.repository.clubboardservice;

import com.walkbuddies.backend.domain.clubboardservice.ClubBoardEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubBoardRepository extends JpaRepository<ClubBoardEntity, Long> {
  Optional<ClubBoardEntity>findByClubBoardId(long boardIdx);
}
