package com.walkbuddies.backend.feed.repository;

import com.walkbuddies.backend.feed.domain.FeedEntity;
import com.walkbuddies.backend.member.domain.MemberEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedRepository extends JpaRepository<FeedEntity, Long> {
  Optional<FeedEntity> findByFeedId(Long feedId);
  Page<FeedEntity> findAllByMemberIdAndDeleteYn(Pageable pageable, MemberEntity entity, Integer deleteYn);
}
