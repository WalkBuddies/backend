package com.walkbuddies.backend.admin.service;

import com.walkbuddies.backend.admin.dto.MemberDetailDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminMemberService {

    /**
     * 모든 회원 정보를 리스트로 불러오는 메서드
     * @return
     */
    List<MemberDetailDto> getMemberList();

    /**
     * 회원 식별자를 통해 그 회원의 정보를 불러오는 메서드
     * @param memberId
     * @return
     */
    MemberDetailDto getMemberDetail(Long memberId);

    /**
     * 파라미터 이름에 해당하는 모든 회원의 정보를 리스트로 불러오는 메서드
     * @param name
     * @return
     */
    List<MemberDetailDto> getMemberName(String name);

    /**
     * 닉네임에 해당하는 회원의 정보를 불러오는 메서드
     * @param nickName
     * @return
     */
    MemberDetailDto getMemberNickName(String nickName);

}
