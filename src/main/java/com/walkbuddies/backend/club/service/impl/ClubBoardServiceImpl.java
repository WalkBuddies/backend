package com.walkbuddies.backend.club.service.impl;

import com.walkbuddies.backend.club.domain.ClubBoardCommentEntity;
import com.walkbuddies.backend.club.domain.ClubBoardEntity;
import com.walkbuddies.backend.club.domain.ClubEntity;
import com.walkbuddies.backend.club.domain.ClubPreface;
import com.walkbuddies.backend.club.dto.ClubPrefaceDto;
import com.walkbuddies.backend.club.dto.PrefaceConvertDtoEntity;
import com.walkbuddies.backend.club.dto.clubboard.ClubBoardDto;
import com.walkbuddies.backend.club.dto.clubboard.ClubBoardConvertDtoEntity;
import com.walkbuddies.backend.club.repository.ClubBoardCommentRepository;
import com.walkbuddies.backend.club.repository.ClubBoardRepository;
import com.walkbuddies.backend.club.repository.ClubPrefaceRepository;
import com.walkbuddies.backend.club.repository.ClubRepository;
import com.walkbuddies.backend.club.service.ClubBoardService;
import com.walkbuddies.backend.common.dto.FileDto;
import com.walkbuddies.backend.common.service.FileService;
import com.walkbuddies.backend.exception.impl.NoPostException;
import com.walkbuddies.backend.exception.impl.NoResultException;
import com.walkbuddies.backend.exception.impl.NotFoundClubException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClubBoardServiceImpl implements ClubBoardService {

  private final ClubBoardRepository clubBoardRepository;
  private final ClubPrefaceRepository clubPrefaceRepository;
  private final ClubRepository clubRepository;
  private final PrefaceConvertDtoEntity prefaceConvertDtoEntity;
  private final ClubBoardCommentRepository boardCommentRepository;

  private final ClubBoardConvertDtoEntity clubBoardConvertDtoEntity;
  private final FileService fileService;

  /**
   * 게시글 쓰기 파일 있으면 파일업로드 후 dto 받아서 등록
   *
   * @param fileId       파일목록
   * @param clubBoardDto 게시글 dto
   * @return
   */
  @Override
  @Transactional
  public ClubBoardDto createPost(List<Long> fileId, ClubBoardDto clubBoardDto) {
    clubBoardDto.setFileYn(0);
    List<FileDto> fileDtos;
    if (fileId != null) {

      fileDtos = fileService.findFilesById(fileId);
      clubBoardDto.setFileYn(1);
      clubBoardDto.setFileId(fileDtos);
    }

    clubBoardDto.setDeleteYn(0);
    clubBoardDto.setCreateAt(LocalDateTime.now());

    ClubBoardEntity result = clubBoardRepository.save(
        clubBoardConvertDtoEntity.dtoToEntity(clubBoardDto));
    log.info("동호회게시판 글 등록: " + result.getClubBoardId());
    return clubBoardConvertDtoEntity.entityToDto(result);

  }

  /**
   * 게시글 상세보기 삭제여부 체크후 0일 시 반환
   *
   * @param boardId 게시글번호
   * @return clubBoardDto
   */

  @Override
  public ClubBoardDto getPost(Long boardId) {
    ClubBoardEntity entity = getBoardEntity(boardId);

    if (entity.getDeleteYn() == 1) {
      log.warn("게시글 오류: noPostException");
      throw new NoPostException();

    }
    return clubBoardConvertDtoEntity.entityToDto(entity);
  }

  /**
   * 게시글목록 보기 (전체)
   * @param pageable (page 필요)
   * @return
   */
  @Override
  public Page<ClubBoardDto> postList(Pageable pageable, Long clubId, Integer deleteYn) {
    ClubEntity entity = getClubEntity(clubId);

    return clubBoardRepository.findAllByClubIdAndDeleteYn(pageable, entity, deleteYn)
        .map(clubBoardConvertDtoEntity::entityToDto);
  }

    /**
     * 게시글목록 보기 (검색)
     * @param pageable (현재페이지 page)
     * @param clubId
     * @param keyword
     * @param type
     * @param deleteYn
     * @return
     */
  @Override
  public Page<ClubBoardDto> postList(Pageable pageable, Long clubId, String keyword, String type,
      Integer deleteYn) {
    ClubEntity entity = getClubEntity(clubId);
    Page<ClubBoardDto> result = switch (type) {
      case "제목" ->
          clubBoardRepository.findByClubIdAndTitleContainingAndDeleteYn(pageable, entity, keyword,
                  deleteYn)
              .map(clubBoardConvertDtoEntity::entityToDto);
      case "작성자" ->
          clubBoardRepository.findByClubIdAndNicknameContainingAndDeleteYn(pageable, entity,
                  keyword, deleteYn)
              .map(clubBoardConvertDtoEntity::entityToDto);
      case "내용" ->
          clubBoardRepository.findByClubIdAndContentContainingAndDeleteYn(pageable, entity, keyword,
                  deleteYn)
              .map(clubBoardConvertDtoEntity::entityToDto);
      case "말머리" ->
          clubBoardRepository.findByClubIdAndPrefaceAndDeleteYn(pageable, entity, keyword, deleteYn)
              .map(clubBoardConvertDtoEntity::entityToDto);
      default -> throw new NoResultException();
    };

    if (!result.hasContent()) {
      throw new NoResultException();
    }

    return result;
  }

  /**
   * 게시글 수정 to-be : 업로드 파일 수정
   *
   * @param clubBoardDto 수정 dto
   * @param fileId       파일id 목록
   * @return 수정된 dto
   */
  @Override
  public ClubBoardDto updatePost(ClubBoardDto clubBoardDto, List<Long> fileId) {
    ClubBoardEntity entity = getBoardEntity(clubBoardDto.getClubBoardId());
    if (fileId.isEmpty()) {
      clubBoardDto.setFileYn(0);
      clubBoardDto.setFileId(null);
    } else {
      List<FileDto> fileDtos = fileService.findFilesById(fileId);
      clubBoardDto.setFileId(fileDtos);
      clubBoardDto.setFileYn(1);
    }

    Optional<ClubPreface> op = clubPrefaceRepository.findByPrefaceId(clubBoardDto.getPrefaceId());
    if (op.isEmpty()) {
      log.warn("말머리가 존재하지 않음");
      throw new NoResultException();
    }
    ClubPreface preface = op.get();
    entity.update(clubBoardDto, preface);
    clubBoardRepository.save(entity);

    return clubBoardConvertDtoEntity.entityToDto(entity);
  }

  /**
   * 게시글 삭제(deleteYn 1로 수정 후 저장)
   *
   * @param boardId 원글 번호
   */
  @Override
  @Transactional
  public void deletePost(Long boardId) {
    ClubBoardEntity entity = getBoardEntity(boardId);
    entity.changeDeleteYn(1);
    clubBoardRepository.save(entity);
    log.info("동호회 게시판 글 삭제 완료:" + boardId);
    Optional<List<ClubBoardCommentEntity>> op = boardCommentRepository.findAllByClubBoardId(entity);
    if (op.isEmpty()) {
      return;
    }
    List<ClubBoardCommentEntity> entities = op.get();
    for (ClubBoardCommentEntity et : entities) {
      et.changeDeleteYn(1);
      boardCommentRepository.save(et);
    }

  }

  /**
   * 게시글 entity 불러오기
   *
   * @param boardId 원글번호
   * @return clubBoardEntity
   */
  @Override
  public ClubBoardEntity getBoardEntity(Long boardId) {
    Optional<ClubBoardEntity> op = clubBoardRepository.findByClubBoardId(boardId);

    if (op.isEmpty()) {
      log.warn("동호회게시판 글이 존재하지 않음:" + boardId);
      throw new NoPostException();
    }

    return op.get();
  }

  /**
   * @param clubId 동호회id
   * @return clubPrefaceDto list
   */
  @Override
  public List<ClubPrefaceDto> getClubPreface(Long clubId) {
    Optional<ClubEntity> opClub = clubRepository.findByClubId(clubId);
    if (opClub.isEmpty()) {
      log.warn("클럽이 존재하지 않음: " + clubId);
      throw new NotFoundClubException();
    }
    ClubEntity entity = opClub.get();

    Optional<List<ClubPreface>> opClubPreface = clubPrefaceRepository.findAllByClubId(entity);
    if (opClubPreface.isEmpty()) {
      throw new NoResultException();
    }

    return opClubPreface.get().stream()
        .map(prefaceConvertDtoEntity::prefaceEntityToDto).toList();


  }


  public ClubEntity getClubEntity(Long clubId) {
    Optional<ClubEntity> op = clubRepository.findByClubId(clubId);
    if (op.isEmpty()) {
      log.warn("클럽이 존재하지 않음: " + clubId);
      throw new NotFoundClubException();
    }

    return op.get();
  }

}
