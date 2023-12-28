package com.walkbuddies.backend.club.dto.clubboard;

import com.walkbuddies.backend.club.domain.ClubBoardEntity;
import com.walkbuddies.backend.club.repository.ClubRepository;
import com.walkbuddies.backend.club.repository.ClubPrefaceRepository;
import com.walkbuddies.backend.common.domain.FileEntity;
import com.walkbuddies.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClubBoardConvertDtoEntity {
  private final ClubRepository clubRepository;
  private final MemberRepository memberRepository;
  private final ClubPrefaceRepository clubPrefaceRepository;
  private final FileEntity fileEntity;
  public ClubBoardEntity dtoToEntity(ClubBoardDto dto) {
    return ClubBoardEntity.builder()
        .clubBoardId(dto.getClubBoardId())
        .clubId(clubRepository.findByClubId(dto.getClubId()).get())
        .memberId(memberRepository.findByMemberId(dto.getMemberId()).get())
        .nickname(dto.getNickname())
        .fileId(fileEntity.dtoListToEntityList(dto.getFileId()))
        .title(dto.getTitle())
        .content(dto.getContent())
        .createAt(dto.getCreateAt())
        .updateAt(dto.getUpdateAt())
        .deleteAt(dto.getDeleteAt())
        .noticeYn(dto.getNoticeYn())
        .deleteYn(dto.getDeleteYn())
        .fileYn(dto.getFileYn())
        .preface(clubPrefaceRepository.findByPrefaceId(dto.getPrefaceId()).get())
        .build();
  }
  public ClubBoardDto entityToDto(ClubBoardEntity entity) {
    return ClubBoardDto.builder()
        .clubBoardId(entity.getClubBoardId())
        .fileId(fileEntity.entityListToDtoList(entity.getFileId()))
        .clubId(entity.getClubId().getClubId())
        .memberId(entity.getMemberId().getMemberId())
        .nickname(entity.getNickname())
        .title(entity.getTitle())
        .content(entity.getContent())
        .createAt(entity.getCreateAt())
        .updateAt(entity.getUpdateAt())
        .deleteAt(entity.getDeleteAt())
        .noticeYn(entity.getNoticeYn())
        .deleteYn(entity.getDeleteYn())
        .fileYn(entity.getFileYn())
        .prefaceId(entity.getPreface().getPrefaceId())
        .build();
  }
}
