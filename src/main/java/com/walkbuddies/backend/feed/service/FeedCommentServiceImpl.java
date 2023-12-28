package com.walkbuddies.backend.feed.service;

import com.walkbuddies.backend.club.dto.clubboardcomment.RequestDto;
import com.walkbuddies.backend.exception.impl.NoResultException;
import com.walkbuddies.backend.feed.domain.FeedCommentEntity;
import com.walkbuddies.backend.feed.domain.FeedEntity;
import com.walkbuddies.backend.feed.dto.FeedCommentConvertEntityDto;
import com.walkbuddies.backend.feed.dto.FeedCommentDto;
import com.walkbuddies.backend.feed.repository.FeedCommentRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedCommentServiceImpl implements FeedCommentService {
  private final FeedCommentConvertEntityDto convert;
  private final FeedCommentRepository feedCommentRepository;
  private final FeedService feedService;

  /**
   * 댓글쓰기
   * @param feedId 원글번호
   * @param feedCommentDto 댓글입력정보
   * @return
   */
  @Override
  public FeedCommentDto createComment(Long feedId, FeedCommentDto feedCommentDto) {
    feedCommentDto.setFeedId(feedId);
    feedCommentDto.setDeleteYn(0);
    FeedCommentEntity entity = convert.toEntity(feedCommentDto);
    if (feedCommentDto.getParentId() != null) {
      entity.updateParent(getFeedCommentEntity(feedCommentDto.getFeedId()));
    }

    feedCommentRepository.save(entity);

    return convert.toDto(entity);

  }

  /**
   * 댓글목록 불러오기
   * @param pageable 페이지정보
   * @param feedId 원글번호
   * @param deleteYn 삭제여부
   * @return
   */
  @Override
  public Page<FeedCommentDto> getCommentList(Pageable pageable, Long feedId, Integer deleteYn) {
    FeedEntity entity = feedService.getFeedEntity(feedId);
    System.out.println(entity.toString());
    Page<FeedCommentEntity> result = feedCommentRepository.findAllByFeedIdAndDeleteYn(pageable, entity, deleteYn);

    return convert.toPageDto(result);
  }

  /**
   * 댓글수정
   * @param dto 수정댓글내용
   * @return 수정된 댓글 dto
   */
  @Override
  public FeedCommentDto updateComment(FeedCommentDto dto) {
    FeedCommentEntity entity = getFeedCommentEntity(dto.getFeedCommentId());
    entity.updateContent(dto);
    feedCommentRepository.save(entity);

    return convert.toDto(entity);
  }

  /**
   * 댓글 삭제
   * @param commentId
   */
  @Override
  public void deleteComment(Long commentId) {
    FeedCommentEntity entity = getFeedCommentEntity(commentId);
    entity.changeDeleteYn(1);
    feedCommentRepository.save(entity);
  }



  /**
   * 댓글entity 로드
   * @param feedId
   * @return
   */
  @Override
  public FeedCommentEntity getFeedCommentEntity(Long feedId) {
    Optional<FeedCommentEntity> op = feedCommentRepository.findByFeedCommentId(feedId);
    if (op.isEmpty()) {
      throw new NoResultException();
    }
    return op.get();
  }

}
