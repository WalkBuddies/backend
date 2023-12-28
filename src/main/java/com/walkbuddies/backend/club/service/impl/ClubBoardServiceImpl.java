package com.walkbuddies.backend.club.service.impl;

import com.walkbuddies.backend.club.domain.ClubBoardEntity;
import com.walkbuddies.backend.club.domain.ClubEntity;
import com.walkbuddies.backend.club.domain.ClubPreface;
import com.walkbuddies.backend.club.dto.ClubPrefaceDto;
import com.walkbuddies.backend.club.dto.PrefaceConvertDtoEntity;
import com.walkbuddies.backend.club.dto.clubboard.ClubBoardDto;
import com.walkbuddies.backend.club.dto.ClubBoardSearch;
import com.walkbuddies.backend.club.dto.clubboard.ClubBoardConvertDtoEntity;
import com.walkbuddies.backend.club.repository.ClubBoardRepository;
import com.walkbuddies.backend.club.repository.ClubPrefaceRepository;
import com.walkbuddies.backend.club.repository.ClubRepository;
import com.walkbuddies.backend.club.service.ClubBoardService;
import com.walkbuddies.backend.common.domain.FileEntity;
import com.walkbuddies.backend.common.dto.FileDto;
import com.walkbuddies.backend.common.repository.FileRepository;
import com.walkbuddies.backend.common.service.FileService;
import com.walkbuddies.backend.exception.impl.NoPostException;
import com.walkbuddies.backend.exception.impl.NoResultException;
import com.walkbuddies.backend.exception.impl.NotFoundClubException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor

public class ClubBoardServiceImpl implements ClubBoardService {
    private final ClubBoardRepository clubBoardRepository;
    private final ClubPrefaceRepository clubPrefaceRepository;
    private final ClubRepository clubRepository;
    private final PrefaceConvertDtoEntity prefaceConvertDtoEntity;

    private final ClubBoardConvertDtoEntity clubBoardConvertDtoEntity;
    private final FileService fileService;

    /**
     * 게시글 쓰기
     * 파일 있으면 파일업로드 후 dto 받아서 등록
     * @param fileId 파일목록
     * @param clubBoardDto 게시글 dto
     * @return
     */
    @Override
    @Transactional
    public ClubBoardDto createPost(List<Long> fileId, ClubBoardDto clubBoardDto) {
        clubBoardDto.setFileYn(0);
        List<FileDto> fileDtos;
        if (fileId != null) {

            fileDtos = fileService.findFilesById(fileId);
            clubBoardDto.setFileYn(1);
            clubBoardDto.setFileId(fileDtos);
        }

        clubBoardDto.setDeleteYn(0);
        clubBoardDto.setCreateAt(LocalDateTime.now());


        ClubBoardEntity result = clubBoardRepository.save(clubBoardConvertDtoEntity.dtoToEntity(clubBoardDto));
        return clubBoardConvertDtoEntity.entityToDto(result);

    }

    /**
     * 게시글 상세보기
     * 삭제여부 체크후 0일 시 반환
     * @param boardIdx 게시글번호
     * @return clubBoardDto
     */

    @Override
    public ClubBoardDto getPost(Long boardIdx) {
        ClubBoardEntity entity = getBoardEntity(boardIdx);
        List<FileEntity> es = entity.getFileId();
        for (FileEntity fe : es) {
            System.out.println(fe.toString());
        }
        if (entity.getDeleteYn() == 1) {
            throw new NoPostException();
        }
        return clubBoardConvertDtoEntity.entityToDto(entity);
    }

    /**
     *
     * @param pageable (page 필요)
     * @return
     */
    @Override
    public Page<ClubBoardDto> postList(Pageable pageable) {
        return clubBoardRepository.findAllByDeleteYn(pageable, 0).map(clubBoardConvertDtoEntity::entityToDto);
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
                .map(clubBoardConvertDtoEntity::entityToDto);

            case "작성자" -> clubBoardRepository.findByNicknameContainingAndDeleteYn(pageable, search.getKeyword(), 0)
                .map(clubBoardConvertDtoEntity::entityToDto);
            case "내용" -> clubBoardRepository.findByContentContainingAndDeleteYn(pageable, search.getKeyword(), 0)
                .map(clubBoardConvertDtoEntity::entityToDto);
            default -> throw new NoResultException();
        };

        if (!result.hasContent()) {
            throw new NoResultException();
        }


        return result;
    }

    /**
     * 게시글 수정
     * to-be : 업로드 파일 수정
     * @param clubBoardDto 수정 dto
     * @param fileId 파일id 목록
     * @return 수정된 dto
     */
    @Override
    public ClubBoardDto updatePost(ClubBoardDto clubBoardDto, List<Long> fileId) {
        ClubBoardEntity entity = getBoardEntity(clubBoardDto.getClubBoardId());
        if ( fileId.isEmpty()) {
            clubBoardDto.setFileYn(0);
            clubBoardDto.setFileId(null);
        } else {
            List<FileDto> fileDtos = fileService.findFilesById(fileId);
            clubBoardDto.setFileId(fileDtos);
            clubBoardDto.setFileYn(1);
        }

        ClubPreface preface = clubPrefaceRepository.findByPrefaceId(clubBoardDto.getPrefaceId()).get();
        entity.update(clubBoardDto, preface);
        clubBoardRepository.save(entity);

        return clubBoardConvertDtoEntity.entityToDto(entity);
    }

    /**
     * 게시글 삭제(deleteYn 1로 수정 후 저장)
     *
     * @param boardIdx 원글 번호
     */
    @Override
    public void deletePost(Long boardIdx) {
        ClubBoardEntity entity = getBoardEntity(boardIdx);
        entity.changeDeleteYn(1);
        clubBoardRepository.save(entity);
    }

    /**
     * 게시글 entity 불러오기
     * @param boardIdx 원글번호
     * @return clubBoardEntity
     */
    @Override
    public ClubBoardEntity getBoardEntity(Long boardIdx) {
        Optional<ClubBoardEntity> op = clubBoardRepository.findByClubBoardId(boardIdx);

        if (op.isEmpty()) {
            throw new NoPostException();
        }

        return op.get();
    }

    /**
     *
     * @param clubIdx 클럽id
     * @return clubPrefaceDto list
     */
    @Override
    public List<ClubPrefaceDto> getClubPreface(Long clubIdx) {
        Optional<ClubEntity> opClub = clubRepository.findByClubId(clubIdx);
        if (opClub.isEmpty()) {
            throw new NotFoundClubException();
        }
        ClubEntity entity = opClub.get();

        Optional<List<ClubPreface>> opClubPreface = clubPrefaceRepository.findAllByClubId(entity);
        if (opClubPreface.isEmpty()) {
            throw new NoResultException();
        }

        return opClubPreface.get().stream()
            .map(prefaceConvertDtoEntity::prefaceEntityToDto).toList();


    }

    /**
     * 게시글 복구
     * @param boardIdx
     */
    @Override
    public void CluBoardRestore(Long boardIdx) {
        ClubBoardEntity entity = getBoardEntity(boardIdx);
        entity.changeDeleteYn(0);
        clubBoardRepository.save(entity);
    }


}
