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

  ClubBoardDto createPost(List<MultipartFile> files, ClubBoardDto clubBoardDto);

  ClubBoardDto getPost(Long boardIdx);

  Page<ClubBoardDto> postList(Pageable pageable);

  Page<ClubBoardDto> postSearchList(Pageable pageable, ClubBoardSearch search);

  ClubBoardDto updatePost(ClubBoardDto clubBoardDto);

  void deletePost(Long boardIdx);
}
