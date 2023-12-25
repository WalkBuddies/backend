package com.walkbuddies.backend.club.service;

import com.walkbuddies.backend.club.dto.clubboardcomment.RequestDto;
import com.walkbuddies.backend.club.dto.clubboardcomment.ResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface ClubBoardCommentService {
  //create
  ResponseDto createComment(Long boardIdx, RequestDto requestDto);
  //list
  //read
  //update
  //delete
}
