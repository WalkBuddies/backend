package com.walkbuddies.backend.common.service;

import com.walkbuddies.backend.common.domain.FileEntity;
import com.walkbuddies.backend.common.repository.FileRepository;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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


  private String generateSaveFilename(final String filename) {
    String uuid = UUID.randomUUID().toString().replaceAll("-", "");
    String extension = StringUtils.getFilenameExtension(filename);
    return uuid + "." + extension;
  }
  private String getUploadPath(final String addPath) {
    return makeDirectories(UPLOADPATH + File.separator + addPath);
  }

  private String makeDirectories(final String path) {
    File dir = new File(path);
    if (!dir.exists()) {
      dir.mkdirs();
    }
    return dir.getPath();
  }


  @Transactional
  @Override
  public List<FileEntity> uploadFiles(List<MultipartFile> multipartfiles) {
    List<FileEntity> files = new ArrayList<>();
    for (MultipartFile multipartFile : multipartfiles) {
      if (multipartFile.isEmpty()) {
        continue;
      }
      files.add(uploadFile(multipartFile));
    }

    return saveFilesDB(files);
  }

  @Transactional
  public List<FileEntity> saveFilesDB(final List<FileEntity> files) {
    List<FileEntity> savedFiles = new ArrayList<>();
    if (CollectionUtils.isEmpty(files)) {
      return savedFiles;
    }

    for (FileEntity entity : files) {
      savedFiles.add(fileRepository.save(entity));
    }

    return savedFiles;
  }
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
}
