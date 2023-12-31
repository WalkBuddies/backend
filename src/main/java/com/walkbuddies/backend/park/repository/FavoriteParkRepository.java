package com.walkbuddies.backend.park.repository;

import com.walkbuddies.backend.park.domain.FavoriteParkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteParkRepository extends JpaRepository<FavoriteParkEntity, Long> {
    List<FavoriteParkEntity> findByMemberMemberId(Long memberId);

    Optional<FavoriteParkEntity> findByMemberMemberIdAndParkParkId(Long memberId, Long parkId);

    boolean existsByMemberMemberIdAndParkParkId(Long memberId, Long parkId);
}
