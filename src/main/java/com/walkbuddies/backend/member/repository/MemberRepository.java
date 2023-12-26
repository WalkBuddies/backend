package com.walkbuddies.backend.member.repository;

import com.walkbuddies.backend.member.cache.CacheNames;
import com.walkbuddies.backend.member.domain.MemberEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByMemberId(Long memberId);

    boolean existsByNickname(String nickname);

    @Cacheable(cacheNames = CacheNames.USERBYEMAIL, key = "'login'+#p0", unless = "#result==null")
    Optional<MemberEntity> findByEmail(String email);

    Optional<MemberEntity> findByEmailAndName(String email, String name);
}