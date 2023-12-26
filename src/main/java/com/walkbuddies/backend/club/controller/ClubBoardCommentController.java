package com.walkbuddies.backend.club.controller;

import com.walkbuddies.backend.club.dto.clubboardcomment.RequestDto;
import com.walkbuddies.backend.club.dto.clubboardcomment.ResponseDto;
import com.walkbuddies.backend.club.service.ClubBoardCommentService;
import com.walkbuddies.backend.common.response.ListResponse;
import com.walkbuddies.backend.common.response.PageResponse;
import com.walkbuddies.backend.common.response.SingleResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
  public ResponseEntity<SingleResponse<String>> createComment(@PathVariable Long boardIdx, @RequestBody RequestDto requestDto) {

    ResponseDto result = clubBoardCommentService.createComment(boardIdx, requestDto);

    SingleResponse<String> response = new SingleResponse<>(HttpStatus.OK.value(), "작성완료",
        result.toString());

    return ResponseEntity.ok(response);

  }

  //list

  @GetMapping("/{boardIdx}/reply-list")
  public ResponseEntity<PageResponse<Page<ResponseDto>>> commentList(@PageableDefault(page = 0, size = 10, sort = "clubBoardCommentId", direction = Direction.DESC)
       Pageable pageable
      ,@PathVariable Long boardIdx) {

    Page<ResponseDto> result = clubBoardCommentService.getCommentList(pageable, boardIdx);
    PageResponse<Page<ResponseDto>> response = new PageResponse<>(HttpStatus.OK.value(), "댓글조회 완료",
        result);
    return ResponseEntity.ok(response);
  }
  //update
  @PostMapping("/reply-update")
  public ResponseEntity<SingleResponse<String>> updateComment(@RequestBody RequestDto dto) {
    ResponseDto result = clubBoardCommentService.updateComment(dto);
    SingleResponse<String> response = new SingleResponse<>(HttpStatus.OK.value(), "수정완료", result.toString());

    return ResponseEntity.ok(response);
  }
  //delete

  @PostMapping("/{commentId}/reply-delete")
  public ResponseEntity<SingleResponse<String>> deleteComment(@PathVariable Long commentId) {
    clubBoardCommentService.deleteComment(commentId);
    SingleResponse<String> response =  new SingleResponse<>(HttpStatus.OK.value(), "삭제완료", null);

    return ResponseEntity.ok(response);
  }
}