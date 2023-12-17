package com.walkbuddies.backend.member.service;

import com.walkbuddies.backend.member.dto.SignUpDto;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {
    void signUp(SignUpDto member);
}
