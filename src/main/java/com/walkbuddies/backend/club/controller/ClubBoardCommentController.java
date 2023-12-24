package com.walkbuddies.backend.club.controller;

import com.walkbuddies.backend.club.dto.clubboardcomment.RequestDto;
import com.walkbuddies.backend.club.dto.ClubBoardCommentResponse;
import com.walkbuddies.backend.club.dto.clubboardcomment.ResponseDto;
import com.walkbuddies.backend.club.service.ClubBoardCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/club/board")
public class ClubBoardCommentController {
  private final ClubBoardCommentService clubBoardCommentService;

  /**
   * 리플작성
   * @param boardIdx : 원글 번호
   * @param requestDto : 댓글dto
   * @return
   */
  @PostMapping("/{boardIdx}/reply")
  public ResponseEntity<ClubBoardCommentResponse> createComment(@PathVariable Long boardIdx, @RequestBody RequestDto requestDto) {

    ResponseDto result = clubBoardCommentService.createComment(boardIdx, requestDto);

    ClubBoardCommentResponse response = new ClubBoardCommentResponse(HttpStatus.OK.value(), "작성완료", result.toString());

    return ResponseEntity.ok(response);

  }

  //list
  //read
  //update
  //delete
}
