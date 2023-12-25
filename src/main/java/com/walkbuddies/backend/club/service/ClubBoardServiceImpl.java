package com.walkbuddies.backend.club.service;

import com.walkbuddies.backend.club.domain.ClubBoardEntity;
import com.walkbuddies.backend.club.dto.ClubBoardDto;
import com.walkbuddies.backend.club.dto.ClubBoardSearch;
import com.walkbuddies.backend.club.repository.ClubBoardRepository;
import com.walkbuddies.backend.club.repository.ClubRepository;
import com.walkbuddies.backend.common.domain.FileEntity;
import com.walkbuddies.backend.common.dto.FileDto;
import com.walkbuddies.backend.common.service.FileServiceImpl;
import com.walkbuddies.backend.exception.impl.NoPostException;
import com.walkbuddies.backend.exception.impl.NoResultException;
import com.walkbuddies.backend.member.repository.MemberRepository;
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
    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;
    private final FileServiceImpl fileService;

    /**
     * clubboard dto -> entity 변환
     * @param dto
     * @return entity
     */
    private ClubBoardEntity dtoToEntity(ClubBoardDto dto) {
        return ClubBoardEntity.builder()
            .clubBoardId(dto.getClubBoardId())
            .clubId(clubRepository.findByClubId(dto.getClubId()).get())
            .memberId(memberRepository.findByMemberId(dto.getMemberId()).get())
            .nickname(dto.getNickname())
            .fileId(FileEntity.dtoListToEntityList(dto.getFileId()))
            .title(dto.getTitle())
            .content(dto.getContent())
            .createAt(dto.getCreateAt())
            .updateAt(dto.getUpdateAt())
            .deleteAt(dto.getDeleteAt())
            .noticeYn(dto.getNoticeYn())
            .deleteYn(dto.getDeleteYn())
            .fileYn(dto.getFileYn())
            .build();
    }

    /**
     * clubboard entity -> dto 변환
     * @param entity
     * @return
     */
    public static ClubBoardDto entityToDto(ClubBoardEntity entity) {
        return ClubBoardDto.builder()
            .clubBoardId(entity.getClubBoardId())
            .fileId(FileEntity.entityListToDtoList(entity.getFileId()))
            .clubId(entity.getClubId().getClubId())
            .memberId(entity.getMemberId().getMemberId())
            .nickname(entity.getNickname())
            .title(entity.getTitle())
            .content(entity.getContent())
            .createAt(entity.getCreateAt())
            .updateAt(entity.getUpdateAt())
            .deleteAt(entity.getDeleteAt())
            .noticeYn(entity.getNoticeYn())
            .deleteYn(entity.getDeleteYn())
            .fileYn(entity.getFileYn())
            .build();
    }

    /**
     * 파일 있으면 파일업로드 후 dto 받아서 등록
     * @param files 파일목록
     * @param clubBoardDto 게시글 dto
     * @return
     */
    @Override
    @Transactional
    public ClubBoardDto createPost(List<MultipartFile> files, ClubBoardDto clubBoardDto) {
        clubBoardDto.setFileYn(0);
        if (files != null) {
            List<FileEntity> fileEntities = fileService.uploadFiles(files);
            List<FileDto> fileDtos = new ArrayList<>();
            for (FileEntity entity : fileEntities) {
                fileDtos.add(FileEntity.entityToDto(entity));
            }
            clubBoardDto.setFileYn(1);
            clubBoardDto.setFileId(fileDtos);
        }
        clubBoardDto.setDeleteYn(0);
        clubBoardDto.setCreateAt(LocalDateTime.now());


        ClubBoardEntity result = clubBoardRepository.save(dtoToEntity(clubBoardDto));
        System.out.println(result);
        return entityToDto(result);

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
            result = entityToDto(entity);

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
        return clubBoardRepository.findAllByDeleteYn(pageable, 0).map(ClubBoardServiceImpl::entityToDto);
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
                .map(ClubBoardServiceImpl::entityToDto);

            case "작성자" -> clubBoardRepository.findByNicknameContainingAndDeleteYn(pageable, search.getKeyword(), 0)
                .map(ClubBoardServiceImpl::entityToDto);
            case "내용" -> clubBoardRepository.findByContentContainingAndDeleteYn(pageable, search.getKeyword(), 0)
                .map(ClubBoardServiceImpl::entityToDto);
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
        ClubBoardEntity request = dtoToEntity(clubBoardDto);
        ClubBoardEntity updated =  clubBoardRepository.save(request);

        return entityToDto(updated);
    }

    /**
     * 게시글 삭제(deleteYn 1로 수정 후 저장)
     *
     * @param boardIdx 원글 번호
     */
    @Override
    public void deletePost(Long boardIdx) {
        Optional<ClubBoardEntity> result =  clubBoardRepository.findByClubBoardId(boardIdx);
        if (result.isEmpty()) {
            throw new NoPostException();
        }

        ClubBoardDto updateDto = entityToDto(result.get());

        updateDto.setDeleteYn(1);
        updateDto.setDeleteAt(LocalDateTime.now());
        ClubBoardEntity request = dtoToEntity(updateDto);
        clubBoardRepository.save(request);
    }

    @Override
    public ClubBoardEntity getBoardEntity(Long boardIdx) {
        return clubBoardRepository.findByClubBoardId(boardIdx).get();
    }


}
