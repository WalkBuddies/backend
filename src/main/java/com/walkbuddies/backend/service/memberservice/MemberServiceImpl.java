package com.walkbuddies.backend.service.memberservice;

import com.walkbuddies.backend.domain.memberservice.MemberEntity;
import com.walkbuddies.backend.dto.memberservice.SignUpDto;
import com.walkbuddies.backend.repository.memberservice.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public void signUp(SignUpDto dto) {
        MemberEntity member = new MemberEntity(dto);
        memberRepository.save(member);
    }
}
