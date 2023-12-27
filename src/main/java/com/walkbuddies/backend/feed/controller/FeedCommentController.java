package com.walkbuddies.backend.feed.controller;

import com.walkbuddies.backend.common.response.PageResponse;
import com.walkbuddies.backend.common.response.SingleResponse;
import com.walkbuddies.backend.feed.dto.FeedCommentDto;
import com.walkbuddies.backend.feed.service.FeedCommentService;
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
@RequestMapping("/feed/")
public class FeedCommentController {
  private final FeedCommentService feedCommentService;


  /**
   * 리플작성
   * @param boardIdx : 원글 번호
   * @param dto : 댓글dto
   * @return
   */
  @PostMapping("/{boardIdx}/comment")
  public ResponseEntity<SingleResponse<String>> createComment(@PathVariable Long boardIdx, @RequestBody FeedCommentDto dto) {

    FeedCommentDto result = feedCommentService.createComment(boardIdx, dto);

    SingleResponse<String> response = new SingleResponse<>(HttpStatus.OK.value(), "작성완료",
        result.toString());

    return ResponseEntity.ok(response);

  }

  //list

  @GetMapping("/{boardIdx}/comment-list")
  public ResponseEntity<PageResponse<Page<FeedCommentDto>>> commentList(@PageableDefault(page = 0, size = 10, sort = "clubBoardCommentId", direction = Direction.DESC)
                              Pageable pageable, @PathVariable Long boardIdx) {
    Page<FeedCommentDto> result = feedCommentService.getCommentList(pageable, boardIdx);
    PageResponse<Page<FeedCommentDto>> response = new PageResponse<>(HttpStatus.OK.value(), "댓글조회 완료",
        result);
    return ResponseEntity.ok(response);
  }
  //update
  @PostMapping("/comment-update")
  public ResponseEntity<SingleResponse<String>> updateComment(@RequestBody FeedCommentDto dto) {
    FeedCommentDto result = feedCommentService.updateComment(dto);
    SingleResponse<String> response = new SingleResponse<>(HttpStatus.OK.value(), "수정완료", result.toString());

    return ResponseEntity.ok(response);
  }
  //delete

  @PostMapping("/{commentId}/comment-delete")
  public ResponseEntity<SingleResponse<String>> deleteComment(@PathVariable Long commentId) {
    feedCommentService.deleteComment(commentId);
    SingleResponse<String> response =  new SingleResponse<>(HttpStatus.OK.value(), "삭제완료", null);

    return ResponseEntity.ok(response);
  }
}
