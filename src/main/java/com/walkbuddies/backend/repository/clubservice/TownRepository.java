package com.walkbuddies.backend.repository.clubservice;

import com.walkbuddies.backend.domain.clubservice.TownEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TownRepository extends JpaRepository<TownEntity, Long> {

}
