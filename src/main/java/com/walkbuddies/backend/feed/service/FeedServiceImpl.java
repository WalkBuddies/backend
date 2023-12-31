package com.walkbuddies.backend.feed.service;

import com.walkbuddies.backend.common.dto.FileDto;
import com.walkbuddies.backend.common.service.FileService;
import com.walkbuddies.backend.exception.impl.NoPostException;
import com.walkbuddies.backend.feed.domain.FeedCommentEntity;
import com.walkbuddies.backend.feed.domain.FeedEntity;
import com.walkbuddies.backend.feed.dto.FeedConvertEntityDto;
import com.walkbuddies.backend.feed.dto.FeedDto;
import com.walkbuddies.backend.feed.repository.FeedCommentRepository;
import com.walkbuddies.backend.feed.repository.FeedRepository;
import com.walkbuddies.backend.member.domain.MemberEntity;
import com.walkbuddies.backend.member.service.MemberService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedServiceImpl implements FeedService {
  private final FileService fileService;
  private final FeedRepository feedRepository;
  private final FeedConvertEntityDto feedConvertEntityDto;
  private final MemberService memberService;
  private final FeedCommentRepository feedCommentRepository;


  /**
   * 피드 쓰기
   * 파일 있으면 업로드 후 dto에 등록
   * @param fileId multipartfile 리스트
   * @param dto 피드 dto
   * @return
   */
  @Override
  @Transactional
  public FeedDto createFeed(List<Long> fileId, FeedDto dto) {
    dto.setFileYn(0);
    List<FileDto> fileDtos;
    if (fileId != null) {
      fileDtos = fileService.findFilesById(fileId);
      dto.setFileYn(1);
      dto.setFileId(fileDtos);
    }
    dto.setDeleteYn(0);
    dto.setCreateAt(LocalDateTime.now());

    FeedEntity entity = feedRepository.save(feedConvertEntityDto.dtoToEntity(dto));
    log.info("피드 등록 완료: " + entity.getFeedId());
    return feedConvertEntityDto.entityToDto(entity);
  }

  /**
   * 피드 상세보기
   * 삭제여부 체크 후 반환
   * @param feedId 피드번호
   * @return feedDto
   */
  @Override
  public FeedDto getFeed(Long feedId) {
    FeedEntity entity = getFeedEntity(feedId);
    if (entity.getDeleteYn() == 1) {
      throw new NoPostException();
    }

    return feedConvertEntityDto.entityToDto(entity);
  }

  /**
   * 피드목록
   * @param memberId long 멤버 id
   * @param pageable 페이징 객체
   * @param deleteYn 삭제여부 (0, 1)
   * @return
   */
  @Override
  public Page<FeedDto> feedList(Pageable pageable, Long memberId, Integer deleteYn) {
    MemberEntity memberEntity = memberService.getMemberEntity(memberId);
    Page<FeedEntity> result = feedRepository.findAllByMemberIdAndDeleteYn(pageable, memberEntity,
        deleteYn);
    Page<FeedDto> dtos = result.map(feedConvertEntityDto::entityToDto);
    return dtos;
  }

  /**
   * 피드 업데이트
   * @param dto 업데이트 요청 dto
   * @param fileId 수정된 fileId
   * @return 업데이트 후 dto
   */
  @Override
  public FeedDto updateFeed(List<Long> fileId, FeedDto dto) {
    FeedEntity entity = getFeedEntity(dto.getFeedId());
    if (fileId.isEmpty()) {
      dto.setFileYn(0);
      dto.setFileId(null);
    } else {
      List<FileDto> fileDtos = fileService.findFilesById(fileId);
      dto.setFileId(fileDtos);
      dto.setFileYn(1);
    }

    entity.update(dto);
    feedRepository.save(entity);
    log.info("피드 수정 완료: " + entity.getFeedId());

    return feedConvertEntityDto.entityToDto(entity);
  }

  /**
   * 피드 삭제
   * @param feedId 피드번호
   */
  @Override
  public void deleteFeed(Long feedId) {
    FeedEntity entity = getFeedEntity(feedId);
    entity.changeDeleteYn(1);
    feedRepository.save(entity);
    log.info("피드 삭제 완료" + entity.getFeedId());
    Optional<List<FeedCommentEntity>> op = feedCommentRepository.findAllByFeedId(entity);
    if (op.isEmpty()) {
      return;
    }
    List<FeedCommentEntity> entities = op.get();
    for(FeedCommentEntity et : entities) {
      et.changeDeleteYn(1);
      feedCommentRepository.save(et);
    }
  }

  /**
   * 피드 엔티티 로드
   * @param feedId 피드 번호
   * @return FeedEntity
   */
  @Override
  public FeedEntity getFeedEntity(Long feedId) {
    Optional<FeedEntity> op = feedRepository.findByFeedId(feedId);
    if (op.isEmpty()) {
      log.warn("피드가 존재하지 않음: " + feedId);
      throw new NoPostException();
    }

    return op.get();
  }

}
