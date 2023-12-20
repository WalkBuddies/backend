package com.walkbuddies.backend.club.controller;

import com.walkbuddies.backend.club.dto.ClubBoardDto;
import com.walkbuddies.backend.club.service.ClubBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/club/board")
@RequiredArgsConstructor
public class ClubBoardController {
    private final ClubBoardService clubBoardService;

    //create
    @PostMapping("/create")
    public ResponseEntity<?> createBoard(ClubBoardDto clubBoardDto) {
        clubBoardService.createBoard(clubBoardDto);

        return null;
    }

    //read
    @GetMapping("/board-details/{boardIdx}")
    public ResponseEntity<?> getBoard(@PathVariable Long boardIdx) {
        ClubBoardDto result = clubBoardService.getBoard(boardIdx);

        return null;
    }

    //list : 검색기능 포함
    @GetMapping("/list")
    public ResponseEntity<?> getList() {
        clubBoardService.getList();

        return null;
    }
    //update

    @PostMapping("/update")
    public ResponseEntity<?> updateBoard(ClubBoardDto clubBoardDto) {
        clubBoardService.updateBoard(clubBoardDto);

        return null;
    }

    //delete

    @PostMapping("/delete")
    public ResponseEntity<?> deleteBoard(ClubBoardDto clubBoardDto) {
        clubBoardService.deleteBoard(clubBoardDto);

        return null;
    }
}
