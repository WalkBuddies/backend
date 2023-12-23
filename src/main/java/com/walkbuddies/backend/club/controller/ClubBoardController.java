package com.walkbuddies.backend.club.controller;

import com.walkbuddies.backend.club.dto.ClubBoardDto;
import com.walkbuddies.backend.club.dto.ClubBoardResponse;
import com.walkbuddies.backend.club.dto.ClubBoardSearch;
import com.walkbuddies.backend.club.service.ClubBoardService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/club/board")
@RequiredArgsConstructor
public class ClubBoardController {
    private final ClubBoardService clubBoardService;
    //create
    @PostMapping("/write")
    public ResponseEntity<ClubBoardResponse> createBoard(@RequestPart(value = "files", required = false) List<MultipartFile> files
                                                        ,@RequestPart(value = "board") ClubBoardDto clubBoardDto) {
        System.out.println(clubBoardDto.toString());
        ClubBoardDto response = clubBoardService.createPost(files, clubBoardDto);
        ClubBoardResponse result = new ClubBoardResponse(HttpStatus.OK.value(), "작성완료",
            response.toString());

        return ResponseEntity.ok(result);
    }

    //read
    @GetMapping("/board-details/{boardIdx}")
    public ResponseEntity<ClubBoardResponse> readBoard(@PathVariable Long boardIdx) {

        ClubBoardResponse result = new ClubBoardResponse(HttpStatus.OK.value(), "조회 완료",
                                     clubBoardService.getPost(boardIdx).toString());

        return ResponseEntity.ok(result);
    }

    //list : 검색기능 포함
    @GetMapping("/list")
    public ResponseEntity<ClubBoardResponse> boardList(@PageableDefault(page = 0, size = 20, sort = "clubBoardId", direction = Sort.Direction.DESC)
                                                        Pageable pageable,
                                                        @RequestParam(value = "keyword", required = false) String keyword,
                                                        @RequestParam(value = "type", required = false) String type) {
        List<ClubBoardDto> data;
        if (keyword == null) {
            data = clubBoardService.postList(pageable).getContent();
        } else {
            ClubBoardSearch search = ClubBoardSearch.builder()
                .keyword(keyword)
                .type(type)
                .build();
            data = clubBoardService.postSearchList(pageable, search).getContent();
        }

        ClubBoardResponse result = new ClubBoardResponse(HttpStatus.OK.value(), "검색 완료",
            data.toString());

        return ResponseEntity.ok(result);
    }
    //update

    @PostMapping("/update")
    public ResponseEntity<ClubBoardResponse> updateBoard(@RequestBody ClubBoardDto clubBoardDto) {

        ClubBoardDto data = clubBoardService.updatePost(clubBoardDto);

        ClubBoardResponse result = new ClubBoardResponse(HttpStatus.OK.value(), "수정 완료",
            data.toString());

        return ResponseEntity.ok(result);
    }

    //delete

    @GetMapping("/delete/{boardIdx}")
    public ResponseEntity<ClubBoardResponse> deleteBoard(@PathVariable long boardIdx) {
        clubBoardService.deletePost(boardIdx);

        ClubBoardResponse result = new ClubBoardResponse(HttpStatus.OK.value(), "삭제 완료",
            null);

        return ResponseEntity.ok(result);
    }
}
