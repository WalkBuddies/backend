package com.walkbuddies.backend.feed.dto;

import com.walkbuddies.backend.common.domain.FileEntity;
import com.walkbuddies.backend.feed.domain.FeedEntity;
import com.walkbuddies.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FeedConvertEntityDto {
  private final MemberService memberService;
  public FeedEntity dtoToEntity(FeedDto dto) {
    return FeedEntity.builder()
        .feedId(dto.getFeedId())
        .memberId(memberService.getMemberEntity(dto.getMemberId()))
        .fileId(FileEntity.dtoListToEntityList(dto.getFileId()))
        .title(dto.getTitle())
        .content(dto.getContent())
        .createAt(dto.getCreateAt())
        .updateAt(dto.getUpdateAt())
        .deleteAt(dto.getDeleteAt())
        .deleteYn(dto.getDeleteYn())
        .build();
  }

  public FeedDto entityToDto(FeedEntity entity) {
    return FeedDto.builder()
        .feedId(entity.getFeedId())
        .memberId(entity.getMemberId().getMemberId())
        .fileYn(entity.getFileYn())
        .fileId(FileEntity.entityListToDtoList(entity.getFileId()))
        .title(entity.getTitle())
        .content(entity.getContent())
        .createAt(entity.getCreateAt())
        .updateAt(entity.getUpdateAt())
        .deleteAt(entity.getDeleteAt())
        .deleteYn(entity.getDeleteYn())
        .build();
  }

  public Page<FeedDto> toPageFeedDto(Page<FeedEntity> entities) {
    return entities.map(entity -> FeedDto.builder()
        .feedId(entity.getFeedId())
        .memberId(entity.getMemberId().getMemberId())
        .fileYn(entity.getFileYn())
        .fileId(FileEntity.entityListToDtoList(entity.getFileId()))
        .title(entity.getTitle())
        .content(entity.getContent())
        .createAt(entity.getCreateAt())
        .updateAt(entity.getUpdateAt())
        .deleteAt(entity.getDeleteAt())
        .deleteYn(entity.getDeleteYn())
        .build());
  }

}
