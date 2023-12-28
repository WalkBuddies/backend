package com.walkbuddies.backend.club.service.impl;

import com.walkbuddies.backend.club.domain.ClubBoardCommentEntity;
import com.walkbuddies.backend.club.domain.ClubBoardEntity;
import com.walkbuddies.backend.club.dto.clubboardcomment.ClubBoardCommentConvertDtoEntity;
import com.walkbuddies.backend.club.dto.clubboardcomment.RequestDto;
import com.walkbuddies.backend.club.dto.clubboardcomment.ResponseDto;
import com.walkbuddies.backend.club.repository.ClubBoardCommentRepository;
import com.walkbuddies.backend.club.service.ClubBoardCommentService;
import com.walkbuddies.backend.club.service.ClubBoardService;
import com.walkbuddies.backend.exception.impl.NoResultException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubBoardCommentServiceImpl implements ClubBoardCommentService {
  private final ClubBoardCommentConvertDtoEntity convert;
  private final ClubBoardService clubBoardService;

  private final ClubBoardCommentRepository clubBoardCommentRepository;

  /**
   * 댓글쓰기
   * @param boardIdx 원글번호
   * @param requestDto 댓글요청 dto
   * @return
   */
  @Override
  public ResponseDto createComment(Long boardIdx, RequestDto requestDto) {
      requestDto.setClubBoardId(boardIdx);
      requestDto.setDeleteYn(0);
      ClubBoardCommentEntity entity = convert.toEntity(requestDto);
      if (requestDto.getParentId() != null) {
        entity.updateParent(getCommentEntity(requestDto.getParentId()));
      }

      clubBoardCommentRepository.save(entity);

      return convert.toDto(entity);
  }

  /**
   * 댓글목록불러오기
   * @param pageable 페이징정보
   * @param boardId 원글번호
   * @param deleteYn 삭제여부(0, 1)
   * @return
   */
  @Override
  public Page<ResponseDto> getCommentList(Pageable pageable, Long boardId, Integer deleteYn) {
     ClubBoardEntity boardEntity = clubBoardService.getBoardEntity(boardId);
     Page<ClubBoardCommentEntity> result = clubBoardCommentRepository.findAllByClubBoardIdAndDeleteYn(pageable, boardEntity, deleteYn);
    return result.map(convert::toDto);

  }

  /**
   * 댓글수정
   * @param requestDto 댓글번호, 수정내용 필수
   * @return
   */
  @Override
  public ResponseDto updateComment(RequestDto requestDto) {

     ClubBoardCommentEntity entity = getCommentEntity(requestDto.getClubBoardCommentId());
     entity.updateContent(requestDto);
     clubBoardCommentRepository.save(entity);

    return convert.toDto(entity);
  }

  /**
   * 댓글entity 로드
   * @param commentId 댓글번호
   * @return
   */
  public ClubBoardCommentEntity getCommentEntity(Long commentId) {
     Optional <ClubBoardCommentEntity> optional = clubBoardCommentRepository.findByClubBoardCommentId(commentId);
     if (optional.isEmpty()) {
       throw new NoResultException();
     }
    return optional.get();
  }

  /**
   * 댓글삭제
   * @param commentId
   */
  @Override
  public void deleteComment(Long commentId) {
     ClubBoardCommentEntity entity = getCommentEntity(commentId);

     entity.changeDeleteYn(1);

     clubBoardCommentRepository.save(entity);

  }


}
