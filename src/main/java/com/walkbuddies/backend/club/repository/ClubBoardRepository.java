package com.walkbuddies.backend.club.repository;

import com.walkbuddies.backend.club.domain.ClubBoardEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubBoardRepository extends JpaRepository<ClubBoardEntity, Long> {
  Optional<ClubBoardEntity> findByClubBoardId(long boardIdx);
  Page<ClubBoardEntity> findByContentContainingAndDeleteYn(Pageable pageable, String keyword, int deleteYn);
  Page<ClubBoardEntity> findByNicknameContainingAndDeleteYn(Pageable pageable, String keyword, int deleteYn);
  Page<ClubBoardEntity> findByTitleContainingAndDeleteYn(Pageable pageable, String keyword, int deleteYn);
  Page<ClubBoardEntity> findAllByDeleteYn(Pageable pageable, int deleteYn);
}
