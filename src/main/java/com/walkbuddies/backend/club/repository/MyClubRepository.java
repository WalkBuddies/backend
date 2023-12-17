package com.walkbuddies.backend.club.repository;

import com.walkbuddies.backend.club.domain.ClubEntity;
import com.walkbuddies.backend.club.domain.MyClubEntity;
import com.walkbuddies.backend.member.domain.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MyClubRepository extends JpaRepository<MyClubEntity, Long> {

    List<MyClubEntity> findByMemberId(MemberEntity memberEntity);

    Optional<MyClubEntity> findByClubIdAndMemberId(ClubEntity clubEntity, MemberEntity memberEntity);
}
