package com.walkbuddies.backend.club.dto.clubboardcomment;

import com.walkbuddies.backend.club.domain.ClubBoardCommentEntity;
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
}
