package com.walkbuddies.backend.member.repository;

import com.walkbuddies.backend.member.domain.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByMemberId(Long memberId);

    Optional<List<MemberEntity>> findByName(String name);

    Optional<MemberEntity> findByNickname(String nickName);
}