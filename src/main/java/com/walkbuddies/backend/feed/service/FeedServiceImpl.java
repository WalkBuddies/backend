package com.walkbuddies.backend.feed.service;

import com.walkbuddies.backend.common.domain.FileEntity;
import com.walkbuddies.backend.common.dto.FileDto;
import com.walkbuddies.backend.common.service.FileService;
import com.walkbuddies.backend.exception.impl.NoPostException;
import com.walkbuddies.backend.feed.domain.FeedEntity;
import com.walkbuddies.backend.feed.dto.FeedConvertEntityDto;
import com.walkbuddies.backend.feed.dto.FeedDto;
import com.walkbuddies.backend.feed.repository.FeedRepository;
import com.walkbuddies.backend.member.domain.MemberEntity;
import com.walkbuddies.backend.member.service.MemberService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {
  private final FileService fileService;
  private final FeedRepository feedRepository;
  private final FeedConvertEntityDto feedConvertEntityDto;
  private final MemberService memberService;

  /**
   * 피드 쓰기
   * 파일 있으면 업로드 후 dto에 등록
   * @param files multipartfile 리스트
   * @param dto 피드 dto
   * @return
   */
  @Override
  @Transactional
  public FeedDto createFeed(List<MultipartFile> files, FeedDto dto) {
    dto.setFileYn(0);
    if (files != null) {
      List<FileDto> fileDtos = fileService.uploadFiles(files).stream().map(FileEntity::entityToDto).toList();
      dto.setFileYn(1);
      dto.setFileId(fileDtos);
    }
    FeedEntity result = feedRepository.save(feedConvertEntityDto.dtoToEntity(dto));


    return feedConvertEntityDto.entityToDto(result);
  }

  /**
   * 피드 상세보기
   * 삭제여부 체크 후 반환
   * @param feedIdx 피드번호
   * @return feedDto
   */
  @Override
  public FeedDto getFeed(Long feedIdx) {
    FeedEntity entity = getFeedEntity(feedIdx);
    if (entity.getDeleteYn() == 1) {
      throw new NoPostException();
    }

    return feedConvertEntityDto.entityToDto(entity);
  }

  /**
   * 피드목록
   * @param memberId long 멤버 id
   * @param pageable 페이징 객체
   * @return
   */
  @Override
  public Page<FeedDto> feedList(Pageable pageable, Long memberId) {
    MemberEntity memberEntity = memberService.getMemberEntity(memberId);

    return feedConvertEntityDto.toPageFeedDto(
        feedRepository.findAllByMemberIdAndDeleteYn(pageable, memberEntity, 0));
  }

  /**
   * 피드 업데이트
   * @param dto 업데이트 요청 dto
   * @return 업데이트 후 dto
   */
  @Override
  public FeedDto updateFeed(FeedDto dto) {
    FeedEntity entity = getFeedEntity(dto.getFeedId());
    entity.update(dto);
    feedRepository.save(entity);
    return feedConvertEntityDto.entityToDto(entity);
  }

  /**
   * 피드 삭제
   * @param feedIdx 피드번호
   */
  @Override
  public void deleteFeed(Long feedIdx) {
    FeedEntity entity = getFeedEntity(feedIdx);
    entity.changeDeleteYn(1);
    feedRepository.save(entity);
  }

  /**
   * 피드 엔티티 로드
   * @param feedIdx 피드 번호
   * @return
   */
  @Override
  public FeedEntity getFeedEntity(Long feedIdx) {
    Optional<FeedEntity> op = feedRepository.findByFeedId(feedIdx);
    if (op.isEmpty()) {
      throw new NoPostException();
    }

    return op.get();
  }

  /**
   * 피드 복구
   * @param feedIdx
   */
  @Override
  public void restoreFeed(Long feedIdx) {
    FeedEntity entity = getFeedEntity(feedIdx);
    entity.changeDeleteYn(0);
    feedRepository.save(entity);
  }
}
