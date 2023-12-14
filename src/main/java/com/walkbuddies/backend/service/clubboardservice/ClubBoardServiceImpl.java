package com.walkbuddies.backend.service.clubboardservice;

import com.walkbuddies.backend.domain.clubboardservice.ClubBoardEntity;
import com.walkbuddies.backend.dto.clubboardservice.ClubBoardDto;
import com.walkbuddies.backend.repository.clubboardservice.ClubBoardRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClubBoardServiceImpl implements ClubBoardService{
  private final ClubBoardRepository clubBoardRepository;
  private final ModelMapper modelMapper;

  @Override
  public ClubBoardEntity createBoard(ClubBoardDto clubBoardDto) {

    ClubBoardEntity requestEntity = modelMapper.map(clubBoardDto, ClubBoardEntity.class);

    clubBoardRepository.save(requestEntity);

    return null;
  }

  @Override
  public ClubBoardEntity getBoard(Long boardIdx) {
    Optional<ClubBoardEntity> result = clubBoardRepository.findByClubBoardId(boardIdx);

    return null;
  }

  @Override
  public List<ClubBoardEntity> getList() {
    List<ClubBoardEntity> result = clubBoardRepository.findAll();
    return null;
  }



}
