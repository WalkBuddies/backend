package com.walkbuddies.backend.feed.dto;

import com.walkbuddies.backend.feed.domain.FeedCommentEntity;
import com.walkbuddies.backend.feed.service.FeedService;
import com.walkbuddies.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeedCommentConvertEntityDto {
  private final MemberService memberService;
  private final FeedService feedService;
  public FeedCommentDto toDto(FeedCommentEntity entity) {
    return FeedCommentDto.builder()
        .feedCommentId(entity.getFeedCommentId())
        .memberId(entity.getMemberId().getMemberId())
        .parentId(entity.getParentId() == null ? null: entity.getFeedCommentId())
        .feedId(entity.getFeedId().getFeedId())
        .nickname(entity.getMemberId().getNickname())
        .content(entity.getContent())
        .createAt(entity.getCreateAt())
        .updateAt(entity.getUpdateAt())
        .deleteYn(entity.getDeleteYn())
        .deleteAt(entity.getDeleteAt())
        .build();
  }

  public FeedCommentEntity toEntity(FeedCommentDto dto) {
    return FeedCommentEntity.builder()
        .feedCommentId(dto.getFeedCommentId())
        .memberId(memberService.getMemberEntity(dto.getMemberId()))
        .feedId(feedService.getFeedEntity(dto.getFeedId()))
        .content(dto.getContent())
        .createAt(dto.getCreateAt())
        .updateAt(dto.getUpdateAt())
        .deleteYn(dto.getDeleteYn())
        .deleteAt(dto.getDeleteAt())
        .build();
  }

  public Page<FeedCommentDto> toPageDto(Page<FeedCommentEntity> entities) {
    return entities.map(this::toDto);
  }
}
