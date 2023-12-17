package com.walkbuddies.backend.club.service;

import com.walkbuddies.backend.club.domain.ClubBoardEntity;
import com.walkbuddies.backend.club.dto.ClubBoardDto;
import com.walkbuddies.backend.club.repository.ClubBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClubBoardServiceImpl implements ClubBoardService {
    private final ClubBoardRepository clubBoardRepository;
    private final ModelMapper modelMapper;
    private final ClubBoardEntity clubBoardEntity;

    /**
     * 게시글 작성
     *
     * @param clubBoardDto
     */
    @Override
    public void createBoard(ClubBoardDto clubBoardDto) {

        ClubBoardEntity requestEntity = modelMapper.map(clubBoardDto, ClubBoardEntity.class);

        clubBoardRepository.save(requestEntity);

    }

    /**
     * 게시글 상세보기
     *
     * @param boardIdx 게시글번호
     * @return clubDto
     */

    @Override
    public ClubBoardDto getBoard(Long boardIdx) {
        Optional<ClubBoardEntity> optionalEntity = clubBoardRepository.findByClubBoardId(boardIdx);
        ClubBoardDto result = new ClubBoardDto();

        if (optionalEntity.isPresent()) {
            ClubBoardEntity entity = optionalEntity.get();
            result = ClubBoardEntity.entityToDto(entity);
        }

        return result;
    }

    /**
     * 리스트 조회(검색기능 추후 추가)
     *
     * @return clubBoardDto 리스트
     */
    @Override
    public List<ClubBoardDto> getList() {
        List<ClubBoardEntity> entityList = clubBoardRepository.findAll();
        List<ClubBoardDto> result = new ArrayList<>();

        for (ClubBoardEntity entity : entityList) {
            ClubBoardDto dto = ClubBoardEntity.entityToDto(entity);
            result.add(dto);
        }

        return result;
    }

    /**
     * 게시글 수정
     *
     * @param clubBoardDto 수정 dto
     */
    @Override
    public void updateBoard(ClubBoardDto clubBoardDto) {
        clubBoardDto.setUpdateAt(LocalDateTime.now());
        ClubBoardEntity request = ClubBoardEntity.dtoToEntity(clubBoardDto);
        clubBoardRepository.save(request);
    }

    /**
     * 게시글 삭제(deleteYn 1로 수정 후 저장)
     *
     * @param clubBoardDto 원글 dto
     */
    @Override
    public void deleteBoard(ClubBoardDto clubBoardDto) {
        clubBoardDto.setDeleteYn(1);
        ClubBoardEntity request = ClubBoardEntity.dtoToEntity(clubBoardDto);
        clubBoardRepository.save(request);
    }


}
