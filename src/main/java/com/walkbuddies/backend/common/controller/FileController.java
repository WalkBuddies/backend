package com.walkbuddies.backend.common.controller;

import com.walkbuddies.backend.common.dto.FileDto;
import com.walkbuddies.backend.common.response.ListResponse;
import com.walkbuddies.backend.common.response.SingleResponse;
import com.walkbuddies.backend.common.service.FileService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {
  private final FileService fileService;
  @PostMapping("/upload")
  public ResponseEntity<ListResponse<FileDto>> fileUpload(List<MultipartFile> files) {
    List<FileDto> result =  fileService.uploadFiles(files);
    ListResponse<FileDto> response = new ListResponse<>(HttpStatus.CREATED.value(),"파일 업로드 완료",result);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/delete/{fileId}")
  public ResponseEntity<SingleResponse<String>> fileDelete(@PathVariable Long fileId) {
    fileService.deleteOneFile(fileId);
    SingleResponse<String> response = new SingleResponse<>(HttpStatus.OK.value(), "파일 삭제 완료", null);
    return ResponseEntity.ok(response);
  }

}
