package com.walkbuddies.backend.member.service;

import com.walkbuddies.backend.member.dto.MemberDto;
import com.walkbuddies.backend.member.dto.SignUpDto;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {
    MemberDto signUp(SignUpDto member);

    MemberDto verify(String email, String code);
}
