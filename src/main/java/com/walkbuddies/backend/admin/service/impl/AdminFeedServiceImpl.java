package com.walkbuddies.backend.admin.service.impl;

import com.walkbuddies.backend.admin.service.AdminFeedService;
import com.walkbuddies.backend.club.domain.ClubBoardCommentEntity;
import com.walkbuddies.backend.club.domain.ClubBoardEntity;
import com.walkbuddies.backend.club.repository.ClubBoardCommentRepository;
import com.walkbuddies.backend.club.repository.ClubBoardRepository;
import com.walkbuddies.backend.club.service.ClubBoardCommentService;
import com.walkbuddies.backend.club.service.ClubBoardService;
import com.walkbuddies.backend.feed.domain.FeedCommentEntity;
import com.walkbuddies.backend.feed.domain.FeedEntity;
import com.walkbuddies.backend.feed.repository.FeedCommentRepository;
import com.walkbuddies.backend.feed.repository.FeedRepository;
import com.walkbuddies.backend.feed.service.FeedCommentService;
import com.walkbuddies.backend.feed.service.FeedService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminFeedServiceImpl implements AdminFeedService {
    private final FeedService feedService;
    private final FeedRepository feedRepository;
    private final FeedCommentService feedCommentService;
    private final FeedCommentRepository feedCommentRepository;
    private final ClubBoardService clubBoardService;
    private final ClubBoardRepository clubBoardRepository;
    private final ClubBoardCommentService clubBoardCommentService;
    private final ClubBoardCommentRepository clubBoardCommentRepository;
  /**
   * 피드 복구
   * @param feedId 피드번호
   */
  @Override
  @Transactional
  public void restoreFeed(Long feedId) {
    FeedEntity entity = feedService.getFeedEntity(feedId);
    entity.changeDeleteYn(0);
    feedRepository.save(entity);
    Optional<List<FeedCommentEntity>> op = feedCommentRepository.findAllByFeedId(entity);
    if (op.isEmpty()) {
      return;
    }
    List<FeedCommentEntity> entities = op.get();
    for (FeedCommentEntity et : entities) {
      et.changeDeleteYn(0);
      feedCommentRepository.save(et);
    }
  }
  /**
   * 댓글복구
   * @param commentId
   */
  @Override
  public void restoreFeedComment(Long commentId) {
    FeedCommentEntity entity = feedCommentService.getFeedCommentEntity(commentId);
    entity.changeDeleteYn(0);
    feedCommentRepository.save(entity);
  }

  /**
   * 게시글 복구
   * @param boardId
   */
  @Override
  @Transactional
  public void restoreBoard(Long boardId) {
    ClubBoardEntity entity = clubBoardService.getBoardEntity(boardId);
    entity.changeDeleteYn(0);
    Optional<List<ClubBoardCommentEntity>> op = clubBoardCommentRepository.findAllByClubBoardId(entity);
    clubBoardRepository.save(entity);

    if (op.isEmpty()) {
      return;
    }
    List<ClubBoardCommentEntity> entities = op.get();

    for(ClubBoardCommentEntity et : entities) {
      et.changeDeleteYn(0);
      clubBoardCommentRepository.save(et);
    }
  }

  /**
   * 댓글 복구
   * @param commentId
   */
  @Override
  public void restoreBoardComment(Long commentId) {
    ClubBoardCommentEntity entity = clubBoardCommentService.getCommentEntity(commentId);
    entity.changeDeleteYn(0);
    clubBoardCommentRepository.save(entity);
  }


}
