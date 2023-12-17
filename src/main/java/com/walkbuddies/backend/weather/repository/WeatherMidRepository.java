package com.walkbuddies.backend.weather.repository;

import com.walkbuddies.backend.weather.domain.WeatherMidEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeatherMidRepository extends JpaRepository<WeatherMidEntity, Long> {
    Optional<WeatherMidEntity> findByRegId(String regId);

    Optional<WeatherMidEntity> findByCityName(String cityName);
}
