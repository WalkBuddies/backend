package com.walkbuddies.backend.bookmark.controller;

import com.walkbuddies.backend.bookmark.dto.BookmarkParameter;
import com.walkbuddies.backend.bookmark.service.BookmarkService;
import com.walkbuddies.backend.common.response.ListResponse;
import com.walkbuddies.backend.common.response.SingleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/bookmark/register")
    public SingleResponse bookmarkRegister(@RequestBody BookmarkParameter bookmarkParameter) {

        SingleResponse singleResponse = new SingleResponse(HttpStatus.OK.value(), bookmarkParameter.getTownName() + " 즐겨찾기 등록 완료.",
                bookmarkService.bookmarkResister(bookmarkParameter));

        return singleResponse;
    }

    @GetMapping("/bookmark/myBookmark")
    public ListResponse getMyBookmark(@RequestBody BookmarkParameter bookmarkParameter) {

        ListResponse listResponse = new ListResponse(HttpStatus.OK.value(), "즐겨찾기 목록을 불러왔습니다.",
                bookmarkService.getMyBookmark(bookmarkParameter.getMemberId()));
        return listResponse;
    }

    @PostMapping("/bookmark/delete")
    public SingleResponse bookmarkDelete(@RequestBody BookmarkParameter bookmarkParameter) {

        SingleResponse singleResponse = new SingleResponse(HttpStatus.OK.value(), "즐겨찾기 삭제 완료.",
                bookmarkService.bookmarkDelete(bookmarkParameter.getBookmarkId()));

        return singleResponse;

    }

    @PostMapping("/bookmark/update")
    public SingleResponse bookmarkUpdate(@RequestBody BookmarkParameter bookmarkParameter) {
        SingleResponse singleResponse = new SingleResponse(HttpStatus.OK.value(), "즐겨찾기 수정 완료.",
                bookmarkService.bookmarkUpdate(bookmarkParameter.getBookmarkId(), bookmarkParameter.getBookmarkName()));

        return singleResponse;
    }

    @GetMapping("/bookmark/weatherMid")
    public ListResponse getWeatherMidData(@RequestBody BookmarkParameter bookmarkParameter) {

        ListResponse listResponse = new ListResponse(HttpStatus.OK.value(), "즐겨찾기 지역의 중기 예보 정보 조회 완료.",
                bookmarkService.getWeatheMidData(bookmarkParameter.getBookmarkId()));

        return listResponse;
    }

    @GetMapping("/bookmark/air")
    public SingleResponse getAirData(@RequestBody BookmarkParameter bookmarkParameter) throws IOException, URISyntaxException {

        SingleResponse singleResponse = new SingleResponse(HttpStatus.OK.value(), "즐겨찾기 지역의 미세먼지 정보 조회 완료.",
                bookmarkService.getAirData(bookmarkParameter.getBookmarkId()));

        return singleResponse;
    }
}
