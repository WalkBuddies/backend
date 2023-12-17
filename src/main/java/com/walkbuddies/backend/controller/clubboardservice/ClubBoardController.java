package com.walkbuddies.backend.controller.clubboardservice;

import com.walkbuddies.backend.dto.clubboardservice.ClubBoardDto;
import com.walkbuddies.backend.service.clubboardservice.ClubBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/club")
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
