package com.walkbuddies.backend.club.service;

import com.walkbuddies.backend.club.dto.clubboardcomment.RequestDto;
import com.walkbuddies.backend.club.dto.clubboardcomment.ResponseDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface ClubBoardCommentService {
  //create
  ResponseDto createComment(Long boardIdx, RequestDto requestDto);
  //list
  Page<ResponseDto> getCommentList(Pageable pageable, Long boardIdx);
  //read
  //update
  ResponseDto updateComment(RequestDto requestDto);
  //delete
  void deleteComment(Long commentId);

}
