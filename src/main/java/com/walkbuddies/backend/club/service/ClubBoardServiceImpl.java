package com.walkbuddies.backend.club.service;

import com.walkbuddies.backend.club.domain.ClubBoardEntity;
import com.walkbuddies.backend.club.dto.ClubBoardDto;
import com.walkbuddies.backend.club.dto.ClubBoardSearch;
import com.walkbuddies.backend.club.repository.ClubBoardRepository;
import com.walkbuddies.backend.exception.impl.NoPostException;
import com.walkbuddies.backend.exception.impl.NoResultException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClubBoardServiceImpl implements ClubBoardService {
    private final ClubBoardRepository clubBoardRepository;


    /**
     * 게시글 작성
     * to-be: 파일첨부
     *
     * @param clubBoardDto
     */
    @Override
    public ClubBoardDto createPost(ClubBoardDto clubBoardDto) {
        clubBoardDto.setCreateAt(LocalDateTime.now());
        ClubBoardEntity result = clubBoardRepository.save(ClubBoardEntity.dtoToEntity(clubBoardDto));

        return ClubBoardEntity.entityToDto(result);

    }

    /**
     * 게시글 상세보기
     * to-be: 파일첨부 조회
     * 삭제여부 체크후 0일 시 반환
     * @param boardIdx 게시글번호
     * @return clubDto
     */

    @Override
    public ClubBoardDto getPost(Long boardIdx) {
        Optional<ClubBoardEntity> optionalEntity = clubBoardRepository.findByClubBoardId(boardIdx);
        ClubBoardDto result;

        if (optionalEntity.isPresent()) {
            ClubBoardEntity entity = optionalEntity.get();
            result = ClubBoardEntity.entityToDto(entity);

            if (entity.getDeleteYn() == 1) {
                throw new NoPostException();
            }
        } else {
            throw new NoPostException();
        }

        return result;
    }

    /**
     *
     * @param pageable (page 필요)
     * @return
     */
    @Override
    public Page<ClubBoardDto> postList(Pageable pageable) {

        return clubBoardRepository.findAllByDeleteYn(pageable, 0).map(ClubBoardEntity::entityToDto);
    }

    /**
     *
     * @param pageable (현재페이지 page)
     * @param search (검색키워드 keyword, 검색컬럼 type)
     * @return
     */
    @Override
    public Page<ClubBoardDto> postSearchList(Pageable pageable, ClubBoardSearch search) {
        Page<ClubBoardDto> result = switch (search.getType()) {
            case "제목" -> clubBoardRepository.findByTitleContainingAndDeleteYn(pageable, search.getKeyword(), 0)
                .map(ClubBoardEntity::entityToDto);

            case "작성자" -> clubBoardRepository.findByNicknameContainingAndDeleteYn(pageable, search.getKeyword(), 0)
                .map(ClubBoardEntity::entityToDto);
            case "내용" -> clubBoardRepository.findByContentContainingAndDeleteYn(pageable, search.getKeyword(), 0)
                .map(ClubBoardEntity::entityToDto);
            default -> throw new NoResultException();
        };

        if (!result.hasContent()) {
          throw new NoResultException();
        }

        return result;
    }

    /**
     * 게시글 수정
     *
     * @param clubBoardDto 수정 dto
     * @return 수정된 dto
     */
    @Override
    public ClubBoardDto updatePost(ClubBoardDto clubBoardDto) {
        clubBoardDto.setUpdateAt(LocalDateTime.now());
        ClubBoardEntity request = ClubBoardEntity.dtoToEntity(clubBoardDto);
        ClubBoardEntity updated =  clubBoardRepository.save(request);

        return ClubBoardEntity.entityToDto(updated);
    }

    /**
     * 게시글 삭제(deleteYn 1로 수정 후 저장)
     *
     * @param clubBoardDto 원글 dto
     */
    @Override
    public void deletePost(Long boardIdx) {
        Optional<ClubBoardEntity> result =  clubBoardRepository.findByClubBoardId(boardIdx);
        if (result.isEmpty()) {
            throw new NoPostException();
        }

        ClubBoardDto updateDto = ClubBoardEntity.entityToDto(result.get());

        updateDto.setDeleteYn(1);
        updateDto.setDeleteAt(LocalDateTime.now());
        ClubBoardEntity request = ClubBoardEntity.dtoToEntity(updateDto);
        clubBoardRepository.save(request);
    }


}
