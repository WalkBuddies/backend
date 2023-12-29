package com.walkbuddies.backend.club.controller;

import com.walkbuddies.backend.club.dto.clubboardcomment.RequestDto;
import com.walkbuddies.backend.club.dto.clubboardcomment.ResponseDto;
import com.walkbuddies.backend.club.service.ClubBoardCommentService;
import com.walkbuddies.backend.common.response.PageResponse;
import com.walkbuddies.backend.common.response.SingleResponse;
import com.walkbuddies.backend.member.jwt.JwtTokenUtil;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/club/board")
public class ClubBoardCommentController {
  private final ClubBoardCommentService clubBoardCommentService;
  private final JwtTokenUtil tokenUtil;


  /**
   * 리플작성
   * @param boardId : 원글 번호
   * @param requestDto : 댓글dto
   * @return
   */
  @PostMapping("/{boardId}/comment")
  public ResponseEntity<SingleResponse<ResponseDto>> createComment(@RequestHeader(value = "authorization") String token
      ,@PathVariable Long boardId, @RequestBody RequestDto requestDto) {
    token = token.substring(7);
    String nickname = tokenUtil.getUserInfoFromToken(token).get("nickname", String.class);
    requestDto.setNickname(nickname);
    ResponseDto result = clubBoardCommentService.createComment(boardId, requestDto);

    SingleResponse<ResponseDto> response = new SingleResponse<>(HttpStatus.OK.value(), "작성완료",
        result);

    return ResponseEntity.ok(response);

  }

  //list

  @GetMapping("/{boardId}/comment-list")
  public ResponseEntity<PageResponse<Page<ResponseDto>>> commentList(@PageableDefault(sort = "clubBoardCommentId", direction = Direction.DESC)
       Pageable pageable
      ,@PathVariable Long boardId) {

    Page<ResponseDto> result = clubBoardCommentService.getCommentList(pageable, boardId, 0);
    PageResponse<Page<ResponseDto>> response = new PageResponse<>(HttpStatus.OK.value(), "댓글조회 완료",
        result);
    return ResponseEntity.ok(response);
  }
  //update
  @PostMapping("/comment-update")
  public ResponseEntity<SingleResponse<String>> updateComment(@RequestBody RequestDto dto) {
    ResponseDto result = clubBoardCommentService.updateComment(dto);
    SingleResponse<String> response = new SingleResponse<>(HttpStatus.OK.value(), "수정완료", result.toString());

    return ResponseEntity.ok(response);
  }
  //delete

  @PostMapping("/{commentId}/comment-delete")
  public ResponseEntity<SingleResponse<String>> deleteComment(@PathVariable Long commentId) {
    clubBoardCommentService.deleteComment(commentId);
    SingleResponse<String> response =  new SingleResponse<>(HttpStatus.OK.value(), "삭제완료", null);

    return ResponseEntity.ok(response);
  }


}
