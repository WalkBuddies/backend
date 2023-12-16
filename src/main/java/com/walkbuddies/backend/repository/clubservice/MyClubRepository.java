package com.walkbuddies.backend.repository.clubservice;

import com.walkbuddies.backend.domain.clubservice.ClubEntity;
import com.walkbuddies.backend.domain.clubservice.MyClubEntity;
import com.walkbuddies.backend.domain.memberservice.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MyClubRepository extends JpaRepository<MyClubEntity, Long> {

    List<MyClubEntity> findByMemberId(MemberEntity memberEntity);

    Optional<MyClubEntity> findByClubIdAndMemberId(ClubEntity clubEntity, MemberEntity memberEntity);
}
