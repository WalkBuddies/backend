package com.walkbuddies.backend.service.memberservice;

import com.walkbuddies.backend.dto.memberservice.SignUpDto;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {
    void signUp(SignUpDto member);
}
