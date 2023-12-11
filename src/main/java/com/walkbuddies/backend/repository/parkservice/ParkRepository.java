package com.walkbuddies.backend.repository.parkservice;

import com.walkbuddies.backend.domain.parkservice.ParkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkRepository extends JpaRepository<ParkEntity, Long> {
    Optional<ParkEntity> findByAddress(String address);

    @Query(value = "SELECT park_name, address, longitude, latitude," +
                    "ST_Distance_Sphere(Point(:longitude, :latitude), Point(longitude, latitude)) AS distance " +
                    "FROM park " +
                    "WHERE ST_Distance_Sphere(Point(:longitude, :latitude), Point(longitude, latitude)) < :distance",
            nativeQuery = true)
    List<Object[]> findNearbyParks(float longitude, float latitude, float distance);
}
