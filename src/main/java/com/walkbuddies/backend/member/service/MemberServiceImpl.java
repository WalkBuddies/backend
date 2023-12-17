package com.walkbuddies.backend.member.service;

import com.walkbuddies.backend.member.domain.MemberEntity;
import com.walkbuddies.backend.member.dto.SignUpDto;
import com.walkbuddies.backend.member.repository.MemberRepository;
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
