package com.walkbuddies.backend.club.repository;

import com.walkbuddies.backend.club.domain.ClubEntity;
import com.walkbuddies.backend.club.domain.ClubPreface;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubPrefaceRepository extends JpaRepository<ClubPreface, Long> {
  Optional<ClubPreface> findByPrefaceId(Long prefaceId);
  Optional<List<ClubPreface>> findAllByClubId(ClubEntity clubId);
}
