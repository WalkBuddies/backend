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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
public class FeedController {
  private final FeedService feedService;

  //create
  @PostMapping("/write")
  public ResponseEntity<SingleResponse<FeedDto>> createFeed(@RequestPart(value = "files", required = false) List<Long> files
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
  public ResponseEntity<PageResponse<Page<FeedDto>>> boardList(@PathVariable Long memberId, @PageableDefault(page = 0, size = 20, sort = "feedId", direction = Sort.Direction.DESC)
  Pageable pageable) {
    Page<FeedDto> data = feedService.feedList(pageable, memberId, 0);

    PageResponse<Page<FeedDto>> result = new PageResponse<>(HttpStatus.OK.value(), "검색 완료",
        data);

    return ResponseEntity.ok(result);
  }
  //update

  @PostMapping("/update")
  public ResponseEntity<SingleResponse<FeedDto>> updateBoard(@RequestPart(value = "files", required = false) List<Long> fileId, @RequestPart(value = "board") FeedDto dto) {

    FeedDto data = feedService.updateFeed(fileId, dto);

    SingleResponse<FeedDto> result = new SingleResponse<>(HttpStatus.OK.value(), "수정 완료",
        data);

    return ResponseEntity.ok(result);
  }

  //delete

  @PostMapping("/delete/{feedId}")
  public ResponseEntity<SingleResponse<String>> deleteBoard(@PathVariable long feedId) {
    feedService.deleteFeed(feedId);

    SingleResponse<String> result = new SingleResponse<>(HttpStatus.OK.value(), "삭제 완료",
        null);

    return ResponseEntity.ok(result);
  }



}
