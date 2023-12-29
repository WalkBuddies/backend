package com.walkbuddies.backend.bookmark.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.walkbuddies.backend.bookmark.dto.BookmarkParameter;
import com.walkbuddies.backend.bookmark.service.BookmarkService;
import com.walkbuddies.backend.common.response.ListResponse;
import com.walkbuddies.backend.common.response.SingleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/bookmark/register")
    public ResponseEntity<SingleResponse> bookmarkRegister(@RequestBody BookmarkParameter bookmarkParameter) {

        SingleResponse singleResponse = new SingleResponse(HttpStatus.OK.value(), bookmarkParameter.getTownName() + " 즐겨찾기 등록 완료.",
                bookmarkService.bookmarkRedister(bookmarkParameter));

        return ResponseEntity.ok(singleResponse);
    }

    @GetMapping("/bookmark/myBookmark")
    public ResponseEntity<ListResponse> getMyBookmark(@RequestParam(name = "memberId") Long memberId) {

        ListResponse listResponse = new ListResponse(HttpStatus.OK.value(), "즐겨찾기 목록을 불러왔습니다.",
                bookmarkService.getMyBookmark(memberId));
        return ResponseEntity.ok(listResponse);
    }

    @PostMapping("/bookmark/delete")
    public ResponseEntity<SingleResponse> bookmarkDelete(@RequestParam(name = "bookmarkId") Long bookmarkId) {

        SingleResponse singleResponse = new SingleResponse(HttpStatus.OK.value(), "즐겨찾기 삭제 완료.",
                bookmarkService.bookmarkDelete(bookmarkId));

        return ResponseEntity.ok(singleResponse);

    }

    @PostMapping("/bookmark/update")
    public ResponseEntity<SingleResponse> bookmarkUpdate(@RequestParam(name = "bookmarkId") Long bookmarkId,
                                                         @RequestParam(name = "bookmarkName") String bookmarkName) {
        SingleResponse singleResponse = new SingleResponse(HttpStatus.OK.value(), "즐겨찾기 수정 완료.",
                bookmarkService.bookmarkUpdate(bookmarkId, bookmarkName));

        return ResponseEntity.ok(singleResponse);
    }

    @GetMapping("/bookmark/weatherMid")
    public ResponseEntity<ListResponse> getWeatherMidData(@RequestParam(name = "bookmarkId") Long bookmarkId) {

        ListResponse listResponse = new ListResponse(HttpStatus.OK.value(), "즐겨찾기 지역의 중기 예보 정보 조회 완료.",
                bookmarkService.getWeatheMidData(bookmarkId));

        return ResponseEntity.ok(listResponse);
    }

    @GetMapping("/bookmark/air")
    public ResponseEntity<SingleResponse> getAirData(@RequestParam(name = "bookmarkId") Long bookmarkId) throws IOException, URISyntaxException {

        SingleResponse singleResponse = new SingleResponse(HttpStatus.OK.value(), "즐겨찾기 지역의 미세먼지 정보 조회 완료.",
                bookmarkService.getAirData(bookmarkId));

        return ResponseEntity.ok(singleResponse);
    }
}
