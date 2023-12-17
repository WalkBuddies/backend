package com.walkbuddies.backend.club.service;

import com.walkbuddies.backend.club.dto.ClubBoardDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClubBoardService {

    void createBoard(ClubBoardDto clubBoardDto);

    ClubBoardDto getBoard(Long boardIdx);

    List<ClubBoardDto> getList();

    void updateBoard(ClubBoardDto clubBoardDto);

    void deleteBoard(ClubBoardDto clubBoardDto);
}
