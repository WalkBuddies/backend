package com.walkbuddies.backend.controller.memberservice;

import com.walkbuddies.backend.dto.memberservice.SignUpDto;
import com.walkbuddies.backend.service.memberservice.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpDto dto) {
        memberService.signUp(dto);
        return ResponseEntity.ok("Member signed up successfully.");
    }
}
