package com.walkbuddies.backend.club.repository;

import com.walkbuddies.backend.club.domain.ClubBoardEntity;
import com.walkbuddies.backend.club.domain.ClubEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubBoardRepository extends JpaRepository<ClubBoardEntity, Long> {
  Optional<ClubBoardEntity> findByClubBoardId(long boardIdx);
  Page<ClubBoardEntity> findByClubIdAndContentContainingAndDeleteYn(Pageable pageable, ClubEntity clubId, String keyword, int deleteYn);
  Page<ClubBoardEntity> findByClubIdAndNicknameContainingAndDeleteYn(Pageable pageable, ClubEntity clubId,String keyword, int deleteYn);
  Page<ClubBoardEntity> findByClubIdAndTitleContainingAndDeleteYn(Pageable pageable, ClubEntity clubId, String keyword, int deleteYn);
  Page<ClubBoardEntity> findAllByClubIdAndDeleteYn(Pageable pageable, ClubEntity clubId, int deleteYn);
  Page<ClubBoardEntity> findByClubIdAndPrefaceAndDeleteYn(Pageable pageable, ClubEntity clubId,String keyword, int deleteYn);
}
