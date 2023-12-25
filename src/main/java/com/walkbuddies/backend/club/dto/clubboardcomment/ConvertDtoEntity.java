package com.walkbuddies.backend.club.dto.clubboardcomment;

import com.walkbuddies.backend.club.domain.ClubBoardCommentEntity;
import com.walkbuddies.backend.club.dto.ClubBoardCommentResponse;
import com.walkbuddies.backend.club.repository.ClubBoardCommentRepository;
import com.walkbuddies.backend.club.repository.ClubBoardRepository;
import com.walkbuddies.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import com.walkbuddies.backend.club.repository.ClubBoardRepository;
import com.walkbuddies.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ConvertDtoEntity {

  private final ClubBoardRepository clubBoardRepository;
  private final MemberRepository memberRepository;

  public ClubBoardCommentEntity toEntity(RequestDto requestDto) {

    return ClubBoardCommentEntity.builder()
        .clubBoardCommentId(requestDto.getClubBoardCommentId())
        .clubBoardId(clubBoardRepository.findByClubBoardId(requestDto.getClubBoardId()).get())
        .memberId(memberRepository.findByMemberId(requestDto.getMemberId()).get())
        .nickname(requestDto.getNickname())
        .content(requestDto.getContent())
        .createAt(requestDto.getCreateAt())
        .updateAt(requestDto.getUpdateAt())
        .deleteAt(requestDto.getDeleteAt())
        .deleteYn(requestDto.getDeleteYn())
        .build();
  }

  public ResponseDto toDto(ClubBoardCommentEntity entity) {
    if (entity.getParentId() != null) {
      return ResponseDto.builder()
          .clubBoardCommentId(entity.getClubBoardCommentId())
          .memberId(entity.getMemberId().getMemberId())
          .parentId(entity.getParentId().getClubBoardCommentId())
          .nickname(entity.getNickname())
          .clubBoardId(entity.getClubBoardId().getClubBoardId())
          .content(entity.getContent())
          .createAt(entity.getCreateAt())
          .updateAt(entity.getUpdateAt())
          .deleteYn(entity.getDeleteYn())
          .deleteAt(entity.getDeleteAt())
          .build();
    } else {
      return ResponseDto.builder()
          .clubBoardCommentId(entity.getClubBoardCommentId())
          .memberId(entity.getMemberId().getMemberId())
          .nickname(entity.getNickname())
          .clubBoardId(entity.getClubBoardId().getClubBoardId())
          .content(entity.getContent())
          .createAt(entity.getCreateAt())
          .updateAt(entity.getUpdateAt())
          .deleteYn(entity.getDeleteYn())
          .deleteAt(entity.getDeleteAt())
          .build();
    }
  }
  public Page<ResponseDto> toPageDto(Page<ClubBoardCommentEntity> entities) {
    Page<ResponseDto> result = entities.map(e -> ResponseDto.builder()
        .clubBoardCommentId(e.getClubBoardCommentId())
        .memberId(e.getMemberId().getMemberId())
        .parentId(e.getParentId() == null ? null : e.getParentId().getClubBoardCommentId())
        .nickname(e.getNickname())
        .clubBoardId(e.getClubBoardId().getClubBoardId())
        .content(e.getContent())
        .createAt(e.getCreateAt())
        .updateAt(e.getUpdateAt())
        .deleteYn(e.getDeleteYn())
        .deleteAt(e.getDeleteAt())
        .build());
    return  result;
  }

}
