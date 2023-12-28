package com.walkbuddies.backend.common.service;

import com.walkbuddies.backend.common.domain.FileEntity;
import com.walkbuddies.backend.common.dto.FileDto;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileService {

  /**
   * 파일업로드
   * @param multipartfiles
   * @return fileDto 리스트
   */
  List<FileDto> uploadFiles(List<MultipartFile> multipartfiles);

  /**
   * 파일조회
   * @param fileId 파일id 리스트
   * @return fileDto 리스트
   */
  List<FileDto> findFilesById(List<Long> fileId);

  /**
   * 파일 삭제
   * @param fileId
   */
  void deleteOneFile(Long fileId);
  void deleteFiles(List<FileEntity> entities);
}
