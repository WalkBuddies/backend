package com.walkbuddies.backend.feed.repository;

import com.walkbuddies.backend.feed.domain.FeedCommentEntity;
import com.walkbuddies.backend.feed.domain.FeedEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedCommentRepository extends JpaRepository<FeedCommentEntity, Long> {
  Optional<FeedCommentEntity> findByFeedCommentId(Long FeedId);
  Page<FeedCommentEntity> findAllByFeedIdAndDeleteYn(Pageable pageable, FeedEntity entity, int deleteYn);
}
