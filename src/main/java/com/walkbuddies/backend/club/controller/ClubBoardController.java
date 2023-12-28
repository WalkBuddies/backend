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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/club/board")
@RequiredArgsConstructor
public class ClubBoardController {
    private final ClubBoardService clubBoardService;

    //create
    @PostMapping("/write")
    public ResponseEntity<SingleResponse<ClubBoardDto>> createBoard(@RequestPart(value = "files", required = false) List<Long> fileId
                                                        ,@RequestPart(value = "board") ClubBoardDto clubBoardDto) {
        System.out.println(fileId);
        ClubBoardDto response = clubBoardService.createPost(fileId, clubBoardDto);
        SingleResponse<ClubBoardDto> result = new SingleResponse<>(HttpStatus.CREATED.value(), "작성완료",
            response);

        return ResponseEntity.ok(result);
    }

    //read
    @GetMapping("/board-details/{boardId}")
    public ResponseEntity<SingleResponse<ClubBoardDto>> readBoard(@PathVariable Long boardId) {

        SingleResponse<ClubBoardDto> result = new SingleResponse<>(HttpStatus.OK.value(), "조회 완료",
                                     clubBoardService.getPost(boardId));

        return ResponseEntity.ok(result);
    }

    //list : 검색기능 포함
    @GetMapping("/list/{clubId}")
    public ResponseEntity<PageResponse<Page<ClubBoardDto>>> boardList(@PageableDefault(page = 0, size = 20, sort = "clubBoardId", direction = Sort.Direction.DESC)
                                                        Pageable pageable,
                                                        @PathVariable(value = "clubId") Long clubId,
                                                        @RequestParam(value = "keyword", required = false) String keyword,
                                                        @RequestParam(value = "type", required = false) String type) {
        Page<ClubBoardDto> data;
        if (keyword == null) {
            data = clubBoardService.postList(pageable, clubId, 0);
        } else {
            ClubBoardSearch search = ClubBoardSearch.builder()
                .keyword(keyword)
                .type(type)
                .build();
            data = clubBoardService.postSearchList(pageable, clubId, search, 0);
        }

        PageResponse<Page<ClubBoardDto>> result = new PageResponse<>(HttpStatus.OK.value(), "검색 완료",
            data);

        return ResponseEntity.ok(result);
    }
    //update

    @PostMapping("/update")
    public ResponseEntity<SingleResponse<ClubBoardDto>> updateBoard(@RequestPart(value = "board") ClubBoardDto clubBoardDto, @RequestPart(value = "files", required = false) List<Long> fileId) {

        ClubBoardDto data = clubBoardService.updatePost(clubBoardDto, fileId);

        SingleResponse<ClubBoardDto> result = new SingleResponse<>(HttpStatus.OK.value(), "수정 완료",
            data);

        return ResponseEntity.ok(result);
    }

    //delete

    @PostMapping("/delete/{boardId}")
    public ResponseEntity<SingleResponse<String>> deleteBoard(@PathVariable long boardId) {
        clubBoardService.deletePost(boardId);

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

    @PostMapping("/restore/{boardId}")
    public ResponseEntity<SingleResponse<String>> restoreBoard(@PathVariable long boardId) {
        clubBoardService.CluBoardRestore(boardId);

        SingleResponse<String> result = new SingleResponse<>(HttpStatus.OK.value(), "복구 완료", null);

        return ResponseEntity.ok(result);

    }

    //삭제된 글 조회(어드민용)
    @GetMapping("/deleted-list")
    public ResponseEntity<PageResponse<Page<ClubBoardDto>>> deletedList(@PageableDefault(page = 0, size = 20, sort = "clubBoardId", direction = Sort.Direction.DESC)
    Pageable pageable,
        @PathVariable(value = "clubId") Long clubId,
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(value = "type", required = false) String type) {
        Page<ClubBoardDto> data;
        if (keyword == null) {
            data = clubBoardService.postList(pageable, clubId, 1);
        } else {
            ClubBoardSearch search = ClubBoardSearch.builder()
                .keyword(keyword)
                .type(type)
                .build();
            data = clubBoardService.postSearchList(pageable, clubId, search, 1);
        }

        PageResponse<Page<ClubBoardDto>> result = new PageResponse<>(HttpStatus.OK.value(), "검색 완료",
            data);

        return ResponseEntity.ok(result);
    }
}
