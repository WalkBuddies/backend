package com.walkbuddies.backend.club.service;

import com.walkbuddies.backend.club.domain.ClubBoardCommentEntity;
import com.walkbuddies.backend.club.dto.clubboardcomment.RequestDto;
import com.walkbuddies.backend.club.dto.clubboardcomment.ResponseDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface ClubBoardCommentService {

  /**
   * 댓글쓰기
   * @param boardId
   * @param requestDto
   * @return
   */
  ResponseDto createComment(Long boardId, RequestDto requestDto);

  /**
   * 댓글리스트호출
   * @param pageable
   * @param boardIdx
   * @return
   */
  Page<ResponseDto> getCommentList(Pageable pageable, Long boardIdx, Integer deleteYn);

  /**
   * 댓글수정
   * @param requestDto
   * @return
   */
  ResponseDto updateComment(RequestDto requestDto);

  /**
   * 댓글삭제
   * @param commentId
   */
  void deleteComment(Long commentId);

  /**
   * 댓글복구
   * @param commentId
   */
  void restoreComment(Long commentId);

  /**
   * 댓글entity 로드
   * @param commentId 댓글번호
   * @return
   */
  ClubBoardCommentEntity getCommentEntity(Long commentId);
}
