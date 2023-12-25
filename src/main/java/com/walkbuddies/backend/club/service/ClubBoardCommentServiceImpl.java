package com.walkbuddies.backend.club.service;

import com.walkbuddies.backend.club.domain.ClubBoardCommentEntity;
import com.walkbuddies.backend.club.domain.ClubBoardEntity;
import com.walkbuddies.backend.club.dto.clubboardcomment.ClubBoardCommentConvertDtoEntity;
import com.walkbuddies.backend.club.dto.clubboardcomment.RequestDto;
import com.walkbuddies.backend.club.dto.clubboardcomment.ResponseDto;
import com.walkbuddies.backend.club.repository.ClubBoardCommentRepository;
import com.walkbuddies.backend.club.repository.ClubBoardRepository;
import com.walkbuddies.backend.exception.impl.NoResultException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubBoardCommentServiceImpl implements ClubBoardCommentService {
  private final ClubBoardCommentConvertDtoEntity clubBoardCommentConvertDtoEntity;
  private final ClubBoardRepository clubBoardRepository;
  private final ClubBoardCommentRepository clubBoardCommentRepository;

   @Override
  public ResponseDto createComment(Long boardIdx, RequestDto requestDto) {
      requestDto.setClubBoardId(boardIdx);
      ClubBoardCommentEntity entity = clubBoardCommentConvertDtoEntity.toEntity(requestDto);
      if (requestDto.getParentId() != null) {
        entity.updateParent(clubBoardCommentRepository.findById(requestDto.getParentId()).get());
      }

      clubBoardCommentRepository.save(entity);

      return clubBoardCommentConvertDtoEntity.toDto(entity);
  }

  @Override
  public Page<ResponseDto> getCommentList(Pageable pageable, Long boardIdx) {
    System.out.println("페이지넘버: " + pageable.getPageNumber());
    ClubBoardEntity boardEntity = clubBoardRepository.findByClubBoardId(boardIdx).orElseThrow(
        NoResultException::new);

     Page<ClubBoardCommentEntity> response = clubBoardCommentRepository.findAllByClubBoardIdAndDeleteYn(pageable, boardEntity, 0);

     Page<ResponseDto> result = clubBoardCommentConvertDtoEntity.toPageDto(response);

    System.out.println(result);

    return result;

  }

  @Override
  public ResponseDto updateComment(RequestDto requestDto) {
     Optional<ClubBoardCommentEntity> optional = clubBoardCommentRepository.findByClubBoardCommentId(
         requestDto.getClubBoardCommentId());
     if (optional.isEmpty()) {
       throw new NoResultException();
     }
     ClubBoardCommentEntity entity = optional.get();
     entity.updateContent(requestDto);
     clubBoardCommentRepository.save(entity);

    return clubBoardCommentConvertDtoEntity.toDto(entity);
  }

  @Override
  public void deleteComment(Long commentId) {
     Optional<ClubBoardCommentEntity> optional = clubBoardCommentRepository.findByClubBoardCommentId(commentId);

     if (optional.isEmpty()) {
       throw new NoResultException();
     }
     ClubBoardCommentEntity entity = optional.get();
     entity.delete();
     clubBoardCommentRepository.save(entity);

  }


}
