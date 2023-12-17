package com.walkbuddies.backend.service.clubboardservice;

import com.walkbuddies.backend.domain.clubboardservice.ClubBoardEntity;
import com.walkbuddies.backend.dto.clubboardservice.ClubBoardDto;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface ClubBoardService {

  void createBoard(ClubBoardDto clubBoardDto);
  ClubBoardDto getBoard(Long boardIdx);
  List<ClubBoardDto> getList();
  void updateBoard(ClubBoardDto clubBoardDto);
  void deleteBoard(ClubBoardDto clubBoardDto);
}
