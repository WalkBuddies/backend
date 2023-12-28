package com.walkbuddies.backend.common.service;

import com.walkbuddies.backend.common.domain.FileEntity;
import com.walkbuddies.backend.common.dto.FileDto;
import com.walkbuddies.backend.common.repository.FileRepository;
import com.walkbuddies.backend.exception.impl.NoFileException;
import com.walkbuddies.backend.exception.impl.NoResultException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService{
  private final String UPLOADPATH = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
  private final FileRepository fileRepository;

  /**
   * 파일저장이름 생성
   * @param filename
   * @return
   */
  private String generateSaveFilename(final String filename) {
    String uuid = UUID.randomUUID().toString().replaceAll("-", "");
    String extension = StringUtils.getFilenameExtension(filename);
    return uuid + "." + extension;
  }

  /**
   * 파일업로드 경로생성
   * @param addPath
   * @return
   */
  private String getUploadPath(final String addPath) {
    return makeDirectories(UPLOADPATH + File.separator + addPath);
  }

  /**
   * 파일업로드경로 디렉토리 만들기
   * @param path
   * @return
   */
  private String makeDirectories(final String path) {
    File dir = new File(path);
    if (!dir.exists()) {
      dir.mkdirs();
    }
    return dir.getPath();
  }

  /**
   * 파일업로드
   * @param multipartfiles
   * @return FileDto
   */

  @Transactional
  @Override
  public List<FileDto> uploadFiles(List<MultipartFile> multipartfiles) {
    List<FileEntity> files = new ArrayList<>();
    for (MultipartFile multipartFile : multipartfiles) {
      if (multipartFile.isEmpty()) {
        continue;
      }
      files.add(uploadFile(multipartFile));
    }

    return saveFilesDB(files);
  }

  /**
   * 파일검색
   * @param fileId 파일id 리스트
   * @return
   */
  @Override
  public List<FileDto> findFilesById(List<Long> fileId) {
    List<FileDto> dtos = new ArrayList<>();
    for (Long id : fileId) {
      FileEntity.entityToDto(getFileEntity(id));
    }
    return dtos;
  }

  /**
   * db에 파일정보 저장
   * @param files
   * @return
   */
  @Transactional
  public List<FileDto> saveFilesDB(final List<FileEntity> files) {
    if (CollectionUtils.isEmpty(files)) {
      return null;
    }
    fileRepository.saveAll(files);
    return FileEntity.entityListToDtoList(files);
  }

  /**
   * 서버에 파일 저장
   * @param multipartFile
   * @return
   */
  @Transactional
  public FileEntity uploadFile(MultipartFile multipartFile) {
    String saveName = generateSaveFilename(multipartFile.getOriginalFilename());
    String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
    String uploadPath = getUploadPath(today) + File.separator + saveName;
    File uploadFile = new File(uploadPath);

    try {
      multipartFile.transferTo(uploadFile);

    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return FileEntity.builder()
        .originalName(multipartFile.getOriginalFilename())
        .saveName(saveName)
        .size(multipartFile.getSize())
        .createAt(LocalDateTime.now())
        .build();
  }

  @Override
  public void deleteFiles(List<FileEntity> entities) {
    for (FileEntity entity : entities) {
      String filepath = Paths.get(UPLOADPATH, entity.getCreateAt().format(
          DateTimeFormatter.ofPattern("yyMMdd")), entity.getSaveName()).toString();
      deleteFile(filepath);
    }
  }

  /**
   * 파일삭제
   * @param filePath
   */
  private void deleteFile(String filePath) {
    File file = new File(filePath);
    if (file.exists()) {
      file.delete();
    } else {
      throw new NoFileException();
    }
  }

  /**
   * 파일삭제
   * @param fileid
   */
  @Override
  @Transactional
  public void deleteOneFile(Long fileid) {

    FileEntity entity = getFileEntity(fileid);
    String filepath = Paths.get(UPLOADPATH, entity.getCreateAt().format(DateTimeFormatter.ofPattern("yyMMdd")), entity.getSaveName()).toString();
    deleteFile(filepath);
    fileRepository.delete(entity);
  }

  public FileEntity getFileEntity(Long fileId) {
    Optional<FileEntity> op = fileRepository.findByFileId(fileId);
    if (op.isEmpty()) {
      throw new NoResultException();
    }
    return op.get();
  }
}
