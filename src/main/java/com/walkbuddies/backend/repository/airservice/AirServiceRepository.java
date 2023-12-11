package com.walkbuddies.airservice.repository.airService;

import com.walkbuddies.backend.domain.airservice.AirServiceEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface AirServiceRepository extends JpaRepository<AirServiceEntity, Long> {

  Optional<AirServiceEntity> findByStationCode(int stationCode);

}
