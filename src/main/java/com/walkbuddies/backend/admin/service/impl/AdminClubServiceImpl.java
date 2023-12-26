package com.walkbuddies.backend.admin.service.impl;

import com.walkbuddies.backend.admin.service.AdminClubService;
import com.walkbuddies.backend.club.domain.ClubEntity;
import com.walkbuddies.backend.club.domain.TownEntity;
import com.walkbuddies.backend.club.dto.ClubDto;
import com.walkbuddies.backend.club.repository.ClubRepository;
import com.walkbuddies.backend.club.repository.TownRepository;
import com.walkbuddies.backend.exception.impl.NotFoundClubException;
import com.walkbuddies.backend.exception.impl.NotFoundMemberException;
import com.walkbuddies.backend.member.domain.MemberEntity;
import com.walkbuddies.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AdminClubServiceImpl implements AdminClubService {

    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;
    private final TownRepository townRepository;

    /**
     * 소모임 상태 설정 메서드
     * @param clubId
     * @param suspended
     * @return
     */
    @Transactional
    @Override
    public ClubDto setClubStatus(Long clubId, boolean suspended) {

        ClubEntity clubEntity = getClubEntity(clubId);
        ClubDto clubDto = ClubEntity.entityToDto(clubEntity);

        MemberEntity memberEntity = getMemberEntity(clubDto.getOwnerId());
        TownEntity townEntity = townRepository.findByTownId(clubDto.getTownId());

        ClubEntity updatedClubEntity = ClubEntity.builder()
                .clubId(clubDto.getClubId())
                .clubName(clubDto.getClubName())
                .townId(townEntity)
                .ownerId(memberEntity)
                .members(clubEntity.getMembers())
                .membersLimit(clubDto.getMembersLimit())
                .accessLimit(clubDto.getAccessLimit())
                .needGrant(clubDto.getNeedGrant())
                .regDate(clubEntity.getRegDate())
                .modDate(LocalDate.now())
                .build();
        clubRepository.save(updatedClubEntity);

        return ClubEntity.entityToDto(updatedClubEntity);
    }

    /**
     * 소모임을 삭제하는 메서드
     * @param clubId
     * @return
     */
    @Override
    public ClubDto deleteClub(Long clubId) {

        ClubEntity clubEntity = getClubEntity(clubId);

        clubRepository.delete(clubEntity);

        return ClubEntity.entityToDto(clubEntity);
    }

    private ClubEntity getClubEntity(Long clubId) {
        return clubRepository.findByClubId(clubId)
                .orElseThrow(() -> new NotFoundClubException());
    }

    private MemberEntity getMemberEntity(Long memberId) {
        return memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NotFoundMemberException());
    }
}
