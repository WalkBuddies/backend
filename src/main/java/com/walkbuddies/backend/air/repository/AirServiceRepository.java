package com.walkbuddies.backend.air.repository;

import com.walkbuddies.backend.air.domain.AirServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface AirServiceRepository extends JpaRepository<AirServiceEntity, Long> {

    Optional<AirServiceEntity> findByStationCode(int stationCode);

}
