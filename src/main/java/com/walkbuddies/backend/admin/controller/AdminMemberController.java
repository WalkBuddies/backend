package com.walkbuddies.backend.admin.controller;

import com.walkbuddies.backend.admin.service.AdminMemberService;
import com.walkbuddies.backend.common.response.ListResponse;
import com.walkbuddies.backend.common.response.SingleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminMemberController {

    private final AdminMemberService adminMemberService;

    @GetMapping("/admin/member/list")
    public ResponseEntity<ListResponse> getMemberList() {

        ListResponse listResponse = new ListResponse(HttpStatus.OK.value(), "회원 리스트를 불러왔습니다.",
                adminMemberService.getMemberList());
        return ResponseEntity.ok(listResponse);
    }

    @GetMapping("/admin/member/name")
    public ResponseEntity<ListResponse> getMemberName(@RequestParam(name = "name") String name) {

        ListResponse listResponse = new ListResponse(HttpStatus.OK.value(), "이름 " + name + "을 가진 회원 목록을 불러왔습니다.",
                adminMemberService.getMemberName(name));
        return ResponseEntity.ok(listResponse);
    }

    @GetMapping("/admin/member/detail")
    public ResponseEntity<SingleResponse> getMemberDetail(@RequestParam(name = "memberId") Long memberId) {

        SingleResponse singleResponse = new SingleResponse(HttpStatus.OK.value(), "memberId: " + memberId + "에 해당하는 멤버의 정보를 불러왔습니다.",
                adminMemberService.getMemberDetail(memberId));
        return ResponseEntity.ok(singleResponse);
    }

    @GetMapping("/admin/member/nickName")
    public ResponseEntity<SingleResponse> getMemberNickName(@RequestParam(name = "nickName") String nickName) {

        SingleResponse singleResponse = new SingleResponse(HttpStatus.OK.value(), "닉네임 " + nickName + "을 가진 회원 정보를 불러왔습니다.",
                adminMemberService.getMemberNickName(nickName));
        return ResponseEntity.ok(singleResponse);
    }
}

