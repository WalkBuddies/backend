package com.walkbuddies.backend.member.service;

import com.walkbuddies.backend.member.domain.MemberEntity;
import com.walkbuddies.backend.member.dto.MemberResponse;
import com.walkbuddies.backend.member.dto.SignUpRequest;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {
    MemberResponse signUp(SignUpRequest member);

    MemberResponse verify(String email, String code);

    String login(String email, String password);

    void logout();
}
