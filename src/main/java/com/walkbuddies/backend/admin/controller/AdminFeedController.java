package com.walkbuddies.backend.admin.controller;

import com.walkbuddies.backend.admin.service.AdminFeedService;
import com.walkbuddies.backend.club.dto.clubboard.ClubBoardDto;
import com.walkbuddies.backend.club.dto.clubboard.ClubBoardListDto;
import com.walkbuddies.backend.club.dto.clubboardcomment.ResponseDto;
import com.walkbuddies.backend.club.service.ClubBoardCommentService;
import com.walkbuddies.backend.club.service.ClubBoardService;
import com.walkbuddies.backend.common.response.PageResponse;
import com.walkbuddies.backend.common.response.SingleResponse;
import com.walkbuddies.backend.feed.dto.FeedCommentDto;
import com.walkbuddies.backend.feed.dto.FeedDto;
import com.walkbuddies.backend.feed.service.FeedCommentService;
import com.walkbuddies.backend.feed.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminFeedController {
  private final AdminFeedService adminFeedService;
  private final ClubBoardService clubBoardService;
  private final ClubBoardCommentService clubBoardCommentService;
  private final FeedService feedService;
  private final FeedCommentService feedCommentService;

  @PostMapping("/admin/club/board/restore/{boardId}")
  public ResponseEntity<SingleResponse<String>> restoreBoard(@PathVariable long boardId) {
    adminFeedService.restoreBoard(boardId);

    SingleResponse<String> result = new SingleResponse<>(HttpStatus.OK.value(), "복구 완료", null);

    return ResponseEntity.ok(result);

  }

  //삭제된 글 조회(어드민용)
  @GetMapping("/admin/club/board/deleted-list/{clubId}")
  public ResponseEntity<PageResponse<Page<ClubBoardDto>>> deletedList(@PageableDefault(page = 0, size = 20, sort = "clubBoardId", direction = Sort.Direction.DESC)
  Pageable pageable,
      @PathVariable(value = "clubId") Long clubId,
      @RequestBody ClubBoardListDto listDto) {

    Page<ClubBoardDto> data = clubBoardService.postList(pageable, clubId, listDto.getKeyword(), listDto.getType(), 1);

    PageResponse<Page<ClubBoardDto>> result = new PageResponse<>(HttpStatus.OK.value(), "검색 완료",
        data);

    return ResponseEntity.ok(result);
  }
  //삭제된 댓글 복구
  @PostMapping("/admin/club/board/comment-restore/{commentId}")
  public ResponseEntity<SingleResponse<String>> restoreComment(@PathVariable Long commentId) {
    adminFeedService.restoreBoardComment(commentId);
    SingleResponse<String> response = new SingleResponse<>(HttpStatus.OK.value(), "복구완료", null);

    return ResponseEntity.ok(response);
  }
  //삭제된 댓글 조회

  @GetMapping("/admin/club/board/deleted-comment-list/{boardId}")
  public ResponseEntity<PageResponse<Page<ResponseDto>>> deletedCommentList(@PageableDefault(page = 0, size = 10, sort = "clubBoardCommentId", direction = Direction.DESC)
  Pageable pageable
      ,@PathVariable Long boardId) {

    Page<ResponseDto> result = clubBoardCommentService.getCommentList(pageable, boardId, 1);
    PageResponse<Page<ResponseDto>> response = new PageResponse<>(HttpStatus.OK.value(), "댓글조회 완료",
        result);
    return ResponseEntity.ok(response);
  }
  //개인피드 복원
  @PostMapping("/admin/feed/restore/{feedId}")
  public ResponseEntity<SingleResponse<String>> restoreBoard(@PathVariable Long feedId) {
    adminFeedService.restoreFeed(feedId);

    SingleResponse<String> result = new SingleResponse<>(HttpStatus.OK.value(), "복구 완료", null);

    return ResponseEntity.ok(result);

  }
  //개인피드 삭제글조회
  @GetMapping("/admin/feed/deleted-list/{memberId}")
  public ResponseEntity<PageResponse<Page<FeedDto>>> deletedList(@PathVariable Long memberId, @PageableDefault(page = 0, size = 20, sort = "feedId", direction = Sort.Direction.DESC)
  Pageable pageable) {
    Page<FeedDto> data = feedService.feedList(pageable, memberId, 1);

    PageResponse<Page<FeedDto>> result = new PageResponse<>(HttpStatus.OK.value(), "검색 완료",
        data);

    return ResponseEntity.ok(result);
  }
  //개인피드 댓글 복원
  @PostMapping("/admin/feed/comment-restore/{commentId}")
  public ResponseEntity<SingleResponse<String>> restoreFeedComment(@PathVariable Long commentId) {
    adminFeedService.restoreFeedComment(commentId);
    SingleResponse<String> response = new SingleResponse<>(HttpStatus.OK.value(), "복구완료", null);

    return ResponseEntity.ok(response);
  }
  //개인피드 삭제댓글 조회
  @GetMapping("/admin/feed/deleted-comment-list/{feedId}")
  public ResponseEntity<PageResponse<Page<FeedCommentDto>>> deletedFeedCommentList(@PageableDefault(page = 0, size = 10, sort = "feedCommentId", direction = Direction.DESC)
  Pageable pageable, @PathVariable Long feedId) {
    Page<FeedCommentDto> result = feedCommentService.getCommentList(pageable, feedId, 1);
    PageResponse<Page<FeedCommentDto>> response = new PageResponse<>(HttpStatus.OK.value(), "댓글조회 완료",
        result);
    return ResponseEntity.ok(response);
  }
}
