package com.walkbuddies.backend.feed.controller;

import com.walkbuddies.backend.common.response.PageResponse;
import com.walkbuddies.backend.common.response.SingleResponse;
import com.walkbuddies.backend.feed.dto.FeedDto;
import com.walkbuddies.backend.feed.service.FeedService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
public class FeedController {
  private final FeedService feedService;

  //create
  @PostMapping("/write")
  public ResponseEntity<SingleResponse<FeedDto>> createBoard(@RequestPart(value = "files", required = false) List<MultipartFile> files
      ,@RequestPart(value = "board") FeedDto feedDto) {
    FeedDto response = feedService.createFeed(files, feedDto);
    SingleResponse<FeedDto> result = new SingleResponse<>(HttpStatus.OK.value(), "작성완료",
        response);

    return ResponseEntity.ok(result);
  }

  //read
  @GetMapping("/feed-details/{feedId}")
  public ResponseEntity<SingleResponse<FeedDto>> readBoard(@PathVariable Long feedId) {

    SingleResponse<FeedDto> result = new SingleResponse<>(HttpStatus.OK.value(), "조회 완료",
        feedService.getFeed(feedId));

    return ResponseEntity.ok(result);
  }

  //list : 검색기능 포함
  @GetMapping("/list/{memberId}")
  public ResponseEntity<PageResponse<Page<FeedDto>>> boardList(@PathVariable Long memberId, @PageableDefault(page = 0, size = 20, sort = "clubBoardId", direction = Sort.Direction.DESC)
  Pageable pageable) {
    Page<FeedDto> data = feedService.feedList(pageable, memberId);

    PageResponse<Page<FeedDto>> result = new PageResponse<>(HttpStatus.OK.value(), "검색 완료",
        data);

    return ResponseEntity.ok(result);
  }
  //update

  @PostMapping("/update")
  public ResponseEntity<SingleResponse<FeedDto>> updateBoard(@RequestBody FeedDto dto) {

    FeedDto data = feedService.updateFeed(dto);

    SingleResponse<FeedDto> result = new SingleResponse<>(HttpStatus.OK.value(), "수정 완료",
        data);

    return ResponseEntity.ok(result);
  }

  //delete

  @GetMapping("/delete/{feedId}")
  public ResponseEntity<SingleResponse<String>> deleteBoard(@PathVariable long feedId) {
    feedService.deleteFeed(feedId);

    SingleResponse<String> result = new SingleResponse<>(HttpStatus.OK.value(), "삭제 완료",
        null);

    return ResponseEntity.ok(result);
  }


  @GetMapping("restore/{feedId}")
  public ResponseEntity<SingleResponse<String>> restoreBoard(@PathVariable long feedId) {
    feedService.restoreFeed(feedId);

    SingleResponse<String> result = new SingleResponse<>(HttpStatus.OK.value(), "복구 완료", null);

    return ResponseEntity.ok(result);

  }

}
