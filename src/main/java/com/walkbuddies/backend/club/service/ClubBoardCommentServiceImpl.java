package com.walkbuddies.backend.club.service;

import com.walkbuddies.backend.club.domain.ClubBoardCommentEntity;
import com.walkbuddies.backend.club.dto.clubboardcomment.ConvertDtoEntity;
import com.walkbuddies.backend.club.dto.clubboardcomment.RequestDto;
import com.walkbuddies.backend.club.dto.clubboardcomment.ResponseDto;
import com.walkbuddies.backend.club.repository.ClubBoardCommentRepository;
import com.walkbuddies.backend.club.repository.ClubBoardRepository;
import com.walkbuddies.backend.member.repository.MemberRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubBoardCommentServiceImpl implements ClubBoardCommentService {
  private final ClubBoardRepository clubBoardRepository;
  private final MemberRepository memberRepository;
  private final ConvertDtoEntity convertDtoEntity;

  private final ClubBoardCommentRepository clubBoardCommentRepository;

   @Override
  public ResponseDto createComment(Long boardIdx, RequestDto requestDto) {
      requestDto.setClubBoardId(boardIdx);
      ClubBoardCommentEntity entity = convertDtoEntity.toEntity(requestDto);
      if (requestDto.getParentId() != null) {
        entity.updateParent(clubBoardCommentRepository.findById(requestDto.getParentId()).get());
      }

      ResponseDto result = new ResponseDto();
      clubBoardCommentRepository.save(entity);

      return result.toDto(entity);
  }
}
