package com.walkbuddies.backend.weather.repository;

import com.walkbuddies.backend.club.domain.TownEntity;
import com.walkbuddies.backend.weather.domain.WeatherShortEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeatherShortRepository extends JpaRepository<WeatherShortEntity, Long> {

    Optional<WeatherShortEntity> findByTownAndFcstDateAndFcstTime(TownEntity townEntity, String fcstDate, String fcstTime);

}
