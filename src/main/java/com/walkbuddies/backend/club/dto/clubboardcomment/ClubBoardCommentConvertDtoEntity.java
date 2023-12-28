package com.walkbuddies.backend.club.dto.clubboardcomment;

import com.walkbuddies.backend.club.domain.ClubBoardCommentEntity;
import com.walkbuddies.backend.club.service.impl.ClubBoardServiceImpl;
import com.walkbuddies.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ClubBoardCommentConvertDtoEntity {

  private final ClubBoardServiceImpl clubBoardService;
  private final MemberService memberService;

  public ClubBoardCommentEntity toEntity(RequestDto requestDto) {

    return ClubBoardCommentEntity.builder()
        .clubBoardCommentId(requestDto.getClubBoardCommentId())
        .clubBoardId(clubBoardService.getBoardEntity(requestDto.getClubBoardId()))
        .memberId(memberService.getMemberEntity(requestDto.getMemberId()))
        .nickname(requestDto.getNickname())
        .content(requestDto.getContent())
        .createAt(requestDto.getCreateAt())
        .updateAt(requestDto.getUpdateAt())
        .deleteAt(requestDto.getDeleteAt())
        .deleteYn(requestDto.getDeleteYn())
        .build();
  }

  public ResponseDto toDto(ClubBoardCommentEntity entity) {
    System.out.println(entity.getContent());
      return ResponseDto.builder()
          .clubBoardCommentId(entity.getClubBoardCommentId())
          .memberId(entity.getMemberId().getMemberId())
          .parentId(entity.getParentId() == null ? null : entity.getParentId().getClubBoardCommentId())
          .nickname(entity.getNickname())
          .clubBoardId(entity.getClubBoardId().getClubBoardId())
          .content(entity.getContent())
          .createAt(entity.getCreateAt())
          .updateAt(entity.getUpdateAt())
          .deleteYn(entity.getDeleteYn())
          .deleteAt(entity.getDeleteAt())
          .build();
  }
  public Page<ResponseDto> toPageDto(Page<ClubBoardCommentEntity> entities) {
    return entities.map(this::toDto);
  }

}
