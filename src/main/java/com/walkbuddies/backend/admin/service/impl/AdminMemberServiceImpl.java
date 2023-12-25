package com.walkbuddies.backend.admin.service.impl;

import com.walkbuddies.backend.admin.dto.MemberDetailDto;
import com.walkbuddies.backend.admin.service.AdminMemberService;
import com.walkbuddies.backend.exception.impl.NotFoundNameException;
import com.walkbuddies.backend.exception.impl.NotFoundNickNameException;
import com.walkbuddies.backend.member.domain.MemberEntity;
import com.walkbuddies.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminMemberServiceImpl implements AdminMemberService {

    private final MemberRepository memberRepository;

    /**
     * 모든 회원 정보를 리스트로 불러오는 메서드
     * @return
     */
    @Override
    public List<MemberDetailDto> getMemberList() {

        List<MemberEntity> memberList = memberRepository.findAll();
        List<MemberDetailDto> memberDetailDtos = new ArrayList<>();

        for (MemberEntity memberEntity : memberList) {
            MemberDetailDto memberDetailDto = MemberEntity.entityToDetail(memberEntity);
            memberDetailDtos.add(memberDetailDto);
        }

        return memberDetailDtos;
    }

    /**
     * 회원 식별자를 통해 그 회원의 정보를 불러오는 메서드
     * @param memberId
     * @return
     */
    @Override
    public MemberDetailDto getMemberDetail(Long memberId) {

        Optional<MemberEntity> memberEntity = memberRepository.findByMemberId(memberId);
        MemberEntity member = memberEntity.get();

        return MemberEntity.entityToDetail(member);
    }

    /**
     * 파라미터 이름에 해당하는 모든 회원의 정보를 리스트로 불러오는 메서드
     * @param name
     * @return
     */
    @Override
    public List<MemberDetailDto> getMemberName(String name) {

        Optional<List<MemberEntity>> memberList = memberRepository.findByName(name);
        if (memberList.get().isEmpty()) {
            throw new NotFoundNameException();
        }

        List<MemberDetailDto> memberDetailDtos = new ArrayList<>();

        for (MemberEntity memberEntity : memberList.get()) {
            MemberDetailDto memberDetailDto = MemberEntity.entityToDetail(memberEntity);
            memberDetailDtos.add(memberDetailDto);
        }

        return memberDetailDtos;
    }

    /**
     * 닉네임에 해당하는 회원의 정보를 불러오는 메서드
     * @param nickName
     * @return
     */
    @Override
    public MemberDetailDto getMemberNickName(String nickName) {

        Optional<MemberEntity> memberEntity = memberRepository.findByNickname(nickName);
        if (memberEntity.isEmpty()) {
            throw new NotFoundNickNameException();
        }

        MemberEntity member = memberEntity.get();


        return MemberEntity.entityToDetail(member);
    }

}
