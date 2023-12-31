package com.walkbuddies.backend.common.domain;

import com.walkbuddies.backend.club.domain.ClubBoardEntity;
import com.walkbuddies.backend.common.dto.FileDto;
import com.walkbuddies.backend.feed.domain.FeedEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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



  public FileDto entityToDto(FileEntity entity) {
    return FileDto.builder()
        .fileId(entity.getFileId())
        .originalName(entity.getOriginalName())
        .savedName(entity.getSaveName())
        .createAt(entity.getCreateAt())
        .size(entity.getSize())
        .build();
  }

  public FileEntity dtoToEntity(FileDto dto) {
    return FileEntity.builder()
        .fileId(dto.getFileId())
        .originalName(dto.getOriginalName())
        .saveName(dto.getSavedName())
        .createAt(dto.getCreateAt())
        .size(dto.getSize())
        .build();
  }

  public List<FileDto> entityListToDtoList(List<FileEntity> files) {
    if (files == null) {
      return null;
    }
    List<FileDto> result = new ArrayList<>();
    for (FileEntity entity : files) {
      result.add(entityToDto(entity));
    }
    return result;
  }

  public List<FileEntity> dtoListToEntityList(List<FileDto> files) {
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
