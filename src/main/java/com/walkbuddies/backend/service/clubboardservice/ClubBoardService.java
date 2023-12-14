package com.walkbuddies.backend.service.clubboardservice;

import com.walkbuddies.backend.domain.clubboardservice.ClubBoardEntity;
import com.walkbuddies.backend.dto.clubboardservice.ClubBoardDto;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface ClubBoardService {

  ClubBoardEntity createBoard(ClubBoardDto clubBoardDto);
  ClubBoardEntity getBoard(Long boardIdx);
  List<ClubBoardEntity> getList();
}
