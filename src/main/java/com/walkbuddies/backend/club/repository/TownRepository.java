package com.walkbuddies.backend.club.repository;

import com.walkbuddies.backend.club.domain.TownEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TownRepository extends JpaRepository<TownEntity, Long> {

    TownEntity findByTownId(Long townId);
}
