package com.walkbuddies.backend.member.repository;

import com.walkbuddies.backend.member.domain.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByMemberId(Long memberId);

    boolean existsByNickname(String nickname);

    Optional<MemberEntity> findByEmail(String email);

    Optional<MemberEntity> findByEmailAndName(String email, String name);
}