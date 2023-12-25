package com.walkbuddies.backend.club.repository;

import com.walkbuddies.backend.club.domain.ClubBoardCommentEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubBoardCommentRepository extends JpaRepository<ClubBoardCommentEntity, Long> {

}
