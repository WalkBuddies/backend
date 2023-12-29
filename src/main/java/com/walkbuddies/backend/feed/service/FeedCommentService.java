package com.walkbuddies.backend.feed.service;

import com.walkbuddies.backend.club.dto.clubboardcomment.RequestDto;
import com.walkbuddies.backend.club.dto.clubboardcomment.ResponseDto;
import com.walkbuddies.backend.feed.domain.FeedCommentEntity;
import com.walkbuddies.backend.feed.dto.FeedCommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface FeedCommentService {
  FeedCommentDto createComment(Long memberId, FeedCommentDto dto);
  Page<FeedCommentDto> getCommentList(Pageable pageable, Long boardIdx, Integer deleteYn);
  FeedCommentDto updateComment(FeedCommentDto requestDto);
  void deleteComment(Long commentId);

  FeedCommentEntity getFeedCommentEntity(Long feedId);

}
