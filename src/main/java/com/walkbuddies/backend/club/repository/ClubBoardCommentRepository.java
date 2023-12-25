package com.walkbuddies.backend.club.repository;

import com.walkbuddies.backend.club.domain.ClubBoardCommentEntity;
import com.walkbuddies.backend.club.domain.ClubBoardEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubBoardCommentRepository extends JpaRepository<ClubBoardCommentEntity, Long> {


  Page<ClubBoardCommentEntity> findAllByClubBoardIdAndDeleteYn(Pageable pageable, ClubBoardEntity boardEntity, int deleteYn);

  Optional<ClubBoardCommentEntity> findByClubBoardCommentId(Long clubBoardCommentId);
//  List<ClubBoardCommentEntity> findAllByClubBoardIdAndDeleteYn(Pageable pageable, ClubBoardEntity entity, int deleteYn);

}
