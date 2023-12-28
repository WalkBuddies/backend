package com.walkbuddies.backend.common.repository;

import com.walkbuddies.backend.common.domain.FileEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
  Optional<FileEntity> findByFileId(Long fileId);

}
