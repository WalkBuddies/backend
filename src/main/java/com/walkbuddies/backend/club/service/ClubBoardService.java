package com.walkbuddies.backend.club.service;

import com.walkbuddies.backend.club.dto.ClubBoardDto;
import com.walkbuddies.backend.club.dto.ClubBoardSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClubBoardService {

  ClubBoardDto createPost(ClubBoardDto clubBoardDto);

  ClubBoardDto getPost(Long boardIdx);

  Page<ClubBoardDto> postList(Pageable pageable);

  Page<ClubBoardDto> postSearchList(Pageable pageable, ClubBoardSearch search);

  ClubBoardDto updatePost(ClubBoardDto clubBoardDto);

  void deletePost(Long boardIdx);
}
