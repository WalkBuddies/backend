package com.walkbuddies.backend.common.service;

import com.walkbuddies.backend.common.domain.FileEntity;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileService {

  List<FileEntity> uploadFiles(List<MultipartFile> multipartfiles);
}
