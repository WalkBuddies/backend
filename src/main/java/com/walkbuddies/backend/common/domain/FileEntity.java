package com.walkbuddies.backend.common.domain;

import com.walkbuddies.backend.common.dto.FileDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "file")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FileEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long fileId;
  private String originalName;
  private String saveName;
  private Long size;
  private LocalDateTime createAt;
  private Integer deleteYn;
  private LocalDateTime deleteAt;


  public static FileDto entityToDto(FileEntity entity) {

    return FileDto.builder()
        .fileId(entity.getFileId())
        .originalName(entity.getOriginalName())
        .savedName(entity.getSaveName())
        .createAt(entity.getCreateAt())
        .deleteYn(entity.getDeleteYn())
        .deleteAt(entity.getDeleteAt())
        .size(entity.getSize())
        .build();
  }

  public static FileEntity dtoToEntity(FileDto dto) {
    return FileEntity.builder()
        .fileId(dto.getFileId())
        .originalName(dto.getOriginalName())
        .saveName(dto.getSavedName())
        .createAt(dto.getCreateAt())
        .deleteYn(dto.getDeleteYn())
        .deleteAt(dto.getDeleteAt())
        .size(dto.getSize())
        .build();
  }

  public static List<FileDto> entityListToDtoList(List<FileEntity> files) {
    if (files==null) {
      return null;
    }
    List<FileDto> result = new ArrayList<>();
    for (FileEntity entity : files) {
      result.add(entityToDto(entity));
    }
    return result;
  }

  public static List<FileEntity> dtoListToEntityList(List<FileDto> files) {
    if (files == null) {
      return null;
    }
    List <FileEntity> result = new ArrayList<>();
    for (FileDto dto : files) {
      result.add(dtoToEntity(dto));
    }
    return result;
  }
}
