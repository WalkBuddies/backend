package com.walkbuddies.backend.club.service;

import com.walkbuddies.backend.club.dto.ClubBoardDto;
import com.walkbuddies.backend.club.dto.ClubBoardSearch;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface ClubBoardService {
  /**
   * 파일 있으면 파일업로드 후 dto 받아서 등록
   * @param files 파일목록
   * @param clubBoardDto 게시글 dto
   * @return
   */
  ClubBoardDto createPost(List<MultipartFile> files, ClubBoardDto clubBoardDto);
  /**
   * 게시글 상세보기
   * to-be: 파일첨부 조회
   * 삭제여부 체크후 0일 시 반환
   * @param boardIdx 게시글번호
   * @return clubDto
   */
  ClubBoardDto getPost(Long boardIdx);
  /**
   *
   * @param pageable (page 필요)
   * @return
   */
  Page<ClubBoardDto> postList(Pageable pageable);
  /**
   *
   * @param pageable (현재페이지 page)
   * @param search (검색키워드 keyword, 검색컬럼 type)
   * @return
   */
  Page<ClubBoardDto> postSearchList(Pageable pageable, ClubBoardSearch search);
  /**
   * 게시글 수정
   *
   * @param clubBoardDto 수정 dto
   * @return 수정된 dto
   */
  ClubBoardDto updatePost(ClubBoardDto clubBoardDto);
  /**
   * 게시글 삭제(deleteYn 1로 수정 후 저장)
   *
   * @param boardIdx 원글 번호
   */
  void deletePost(Long boardIdx);
}
