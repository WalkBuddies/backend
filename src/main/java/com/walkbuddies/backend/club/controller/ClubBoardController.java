package com.walkbuddies.backend.club.controller;

import com.walkbuddies.backend.club.domain.ClubPreface;
import com.walkbuddies.backend.club.dto.ClubPrefaceDto;
import com.walkbuddies.backend.club.dto.clubboard.ClubBoardDto;
import com.walkbuddies.backend.club.dto.ClubBoardSearch;
import com.walkbuddies.backend.club.repository.ClubPrefaceRepository;
import com.walkbuddies.backend.club.service.ClubBoardService;
import com.walkbuddies.backend.common.response.ListResponse;
import com.walkbuddies.backend.common.response.PageResponse;
import com.walkbuddies.backend.common.response.SingleResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<SingleResponse<ClubBoardDto>> createBoard(@RequestPart(value = "files", required = false) List<MultipartFile> files
                                                        ,@RequestPart(value = "board") ClubBoardDto clubBoardDto) {
        System.out.println(clubBoardDto.toString());
        ClubBoardDto response = clubBoardService.createPost(files, clubBoardDto);
        SingleResponse<ClubBoardDto> result = new SingleResponse<>(HttpStatus.OK.value(), "작성완료",
            response);

        return ResponseEntity.ok(result);
    }

    //read
    @GetMapping("/board-details/{boardIdx}")
    public ResponseEntity<SingleResponse<ClubBoardDto>> readBoard(@PathVariable Long boardIdx) {

        SingleResponse<ClubBoardDto> result = new SingleResponse<>(HttpStatus.OK.value(), "조회 완료",
                                     clubBoardService.getPost(boardIdx));

        return ResponseEntity.ok(result);
    }

    //list : 검색기능 포함
    @GetMapping("/list")
    public ResponseEntity<PageResponse<Page<ClubBoardDto>>> boardList(@PageableDefault(page = 0, size = 20, sort = "clubBoardId", direction = Sort.Direction.DESC)
                                                        Pageable pageable,
                                                        @RequestParam(value = "keyword", required = false) String keyword,
                                                        @RequestParam(value = "type", required = false) String type) {
        Page<ClubBoardDto> data;
        if (keyword == null) {
            data = clubBoardService.postList(pageable);
        } else {
            ClubBoardSearch search = ClubBoardSearch.builder()
                .keyword(keyword)
                .type(type)
                .build();
            data = clubBoardService.postSearchList(pageable, search);
        }

        PageResponse<Page<ClubBoardDto>> result = new PageResponse<>(HttpStatus.OK.value(), "검색 완료",
            data);

        return ResponseEntity.ok(result);
    }
    //update

    @PostMapping("/update")
    public ResponseEntity<SingleResponse<ClubBoardDto>> updateBoard(@RequestBody ClubBoardDto clubBoardDto) {

        ClubBoardDto data = clubBoardService.updatePost(clubBoardDto);

        SingleResponse<ClubBoardDto> result = new SingleResponse<>(HttpStatus.OK.value(), "수정 완료",
            data);

        return ResponseEntity.ok(result);
    }

    //delete

    @GetMapping("/delete/{boardIdx}")
    public ResponseEntity<SingleResponse<String>> deleteBoard(@PathVariable long boardIdx) {
        clubBoardService.deletePost(boardIdx);

        SingleResponse<String> result = new SingleResponse<>(HttpStatus.OK.value(), "삭제 완료",
            null);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/preface/{clubId}")
    public  ResponseEntity<ListResponse<ClubPrefaceDto>> getPreface(@PathVariable long clubId) {
        List<ClubPrefaceDto> data = clubBoardService.getClubPreface(clubId);
        ListResponse<ClubPrefaceDto> result = new ListResponse<>(HttpStatus.OK.value(), "말머리 로드", data);

        return ResponseEntity.ok(result);
    }
}
