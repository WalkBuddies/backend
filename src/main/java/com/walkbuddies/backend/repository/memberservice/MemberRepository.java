package com.walkbuddies.backend.repository.memberservice;

import com.walkbuddies.backend.domain.memberservice.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
}
