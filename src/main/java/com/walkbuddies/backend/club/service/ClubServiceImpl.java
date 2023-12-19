package com.walkbuddies.backend.club.service;

import com.walkbuddies.backend.club.domain.ClubEntity;
import com.walkbuddies.backend.club.domain.ClubWaitingEntity;
import com.walkbuddies.backend.club.domain.MyClubEntity;
import com.walkbuddies.backend.club.domain.TownEntity;
import com.walkbuddies.backend.club.dto.ClubDto;
import com.walkbuddies.backend.club.dto.ClubJoinInform;
import com.walkbuddies.backend.club.repository.ClubRepository;
import com.walkbuddies.backend.club.repository.ClubWaitingRepository;
import com.walkbuddies.backend.club.repository.MyClubRepository;
import com.walkbuddies.backend.club.repository.TownRepository;
import com.walkbuddies.backend.exception.impl.*;
import com.walkbuddies.backend.member.domain.MemberEntity;
import com.walkbuddies.backend.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {

    private final ClubRepository clubRepository;
    private final MyClubRepository myClubRepository;
    private final MemberRepository memberRepository;
    private final ClubWaitingRepository clubWaitingRepository;
    private final TownRepository townRepository;

    /**
     * 소모임을 생성하는 메서드.
     *
     * @param clubDto
     * @return
     */
    @Transactional
    @Override
    public ClubDto createClub(ClubDto clubDto) {

        Optional<ClubEntity> existingClub = clubRepository.findByClubName(clubDto.getClubName());
        if (existingClub.isPresent()) {
            throw new ExistsClubException();
        }

        MemberEntity member = getMemberEntity(clubDto.getOwnerId());
        if (!clubDto.getTownId().equals(member.getTownId().getTownId())) {
            throw new NotMyTownException();
        }

        ClubEntity clubEntity = ClubEntity.builder()
                .clubName(clubDto.getClubName())
                .townId(member.getTownId())
                .ownerId(member)
                .members(1)
                .membersLimit(clubDto.getMembersLimit())
                .accessLimit(clubDto.getAccessLimit())
                .needGrant(clubDto.getNeedGrant())
                .regDate(LocalDate.now())
                .build();

        clubRepository.save(clubEntity);

        MyClubEntity myClubEntity = MyClubEntity.builder()
                .memberId(member)
                .clubId(clubEntity)
                .authority(1)
                .regDate(LocalDate.now())
                .build();

        myClubRepository.save(myClubEntity);

        return ClubEntity.entityToDto(clubEntity);
    }

    /**
     * 소모임 폐쇄 메서드
     * @param ownerId
     * @param clubId
     * @return
     */
    @Transactional
    @Override
    public ClubDto deleteClub(Long ownerId, Long clubId) {

        ClubEntity clubEntity = getClubEntity(clubId);
        MemberEntity memberEntity = getMemberEntity(ownerId);
        List<MyClubEntity> myClubEntities = myClubRepository.findByClubId(clubEntity);
        List<ClubWaitingEntity> clubWaitingEntities = clubWaitingRepository.findByClubId(clubEntity);
        Optional<ClubEntity> byClubIdAndMemberId = clubRepository.findByClubIdAndOwnerId(clubId, memberEntity);

        if (!clubWaitingEntities.isEmpty()) {
            for (ClubWaitingEntity clubWaitingEntity : clubWaitingEntities) {
                clubWaitingRepository.delete(clubWaitingEntity);
            }
        }

        for (MyClubEntity myClubEntity : myClubEntities) {
            myClubRepository.delete(myClubEntity);
        }

        clubRepository.delete(byClubIdAndMemberId.get());

        return ClubEntity.entityToDto(byClubIdAndMemberId.get());
    }

    /**
     * 소모임 검색 기능 메서드.
     * 검색어가 포함되고 검색이 되도록 한 소모임에 한에서 모두 검색이 됨.
     *
     * @param clubName
     * @return
     */
    @Override
    public List<String> searchClub(Long townId, String clubName) {

        List<ClubEntity> clubEntities = clubRepository.findByClubNameContaining(clubName);
        if (clubEntities.isEmpty()) {
            throw new NotFoundClubException();
        }

        return clubEntities.stream()
                .filter(clubEntity -> clubEntity.getAccessLimit() != 2 && clubEntity.getTownId().getTownId().equals(townId))
                .map(ClubEntity::getClubName)
                .collect(Collectors.toList());
    }

    /**
     * 소모임 가입 요청 기능 메서드
     * 단 가입 조건이 없는 소모임일 경우 바로 가입 가능
     *
     * @param clubJoinInform
     * @return
     */
    @Transactional
    @Override
    public String joinClubRequest(ClubJoinInform clubJoinInform) {

        ClubEntity clubEntity = getClubEntity(clubJoinInform.getClubId());
        MemberEntity memberEntity = getMemberEntity(clubJoinInform.getMemberId());
        Optional<MyClubEntity> optionalMyClub = myClubRepository.findByClubIdAndMemberId(clubEntity, memberEntity);

        if (clubEntity.getMembersLimit() < clubEntity.getMembers() + 1) {
            throw new MemberLimitException();
        }
        if (!clubEntity.getTownId().getTownId().equals(clubJoinInform.getTownId())) {
            throw new NotMyTownException();
        }
        if (optionalMyClub.isPresent()) {
            throw new AlreadyMemberException();
        }

        if (clubEntity.getNeedGrant() == 1) {
            ClubWaitingEntity clubWaitingEntity = ClubWaitingEntity.builder()
                    .clubId(clubEntity)
                    .memberId(memberEntity)
                    .message(clubJoinInform.getMessage())
                    .build();
            clubWaitingRepository.save(clubWaitingEntity);

            MyClubEntity myClubEntity = MyClubEntity.builder()
                    .memberId(memberEntity)
                    .clubId(clubEntity)
                    .authority(3)
                    .regDate(LocalDate.now())
                    .build();
            myClubRepository.save(myClubEntity);

            return "가입 승인 대기!";
        }

        MyClubEntity updatedMyClubEntity = MyClubEntity.builder()
                .memberId(memberEntity)
                .clubId(clubEntity)
                .authority(2)
                .regDate(LocalDate.now())
                .build();
        myClubRepository.save(updatedMyClubEntity);

        ClubEntity updatedClubEntity = ClubEntity.builder()
                .clubId(clubEntity.getClubId())
                .clubName(clubEntity.getClubName())
                .townId(clubEntity.getTownId())
                .ownerId(clubEntity.getOwnerId())
                .members(clubEntity.getMembers() + 1)
                .membersLimit(clubEntity.getMembersLimit())
                .accessLimit(clubEntity.getAccessLimit())
                .needGrant(clubEntity.getNeedGrant())
                .regDate(clubEntity.getRegDate())
                .modDate(LocalDate.now())
                .build();
        clubRepository.save(updatedClubEntity);

        return "가입 완료!";
    }

    /**
     * 소모임 Id를 기반으로 소모임의 가입 신청자를 볼 수 있는 메서드.
     * 신청자의 memberId와 가입시 작성했던 message를 함께 볼 수 있음.
     *
     * @param clubId
     * @return
     */
    @Override
    public List<String> getClubWaitingData(Long clubId) {
        ClubEntity clubEntity = getClubEntity(clubId);
        List<ClubWaitingEntity> clubWaitingEntities = clubWaitingRepository.findByClubId(clubEntity);
        if (clubWaitingEntities.isEmpty()) {
            return Collections.singletonList("가입 신청자가 없습니다.");
        }

        return clubWaitingEntities.stream()
                .map(clubWaitingEntity -> String.format("memberId: %s, message: %s",
                        clubWaitingEntity.getMemberId().getMemberId(),
                        clubWaitingEntity.getMessage()))
                .collect(Collectors.toList());
    }

    /**
     * 소모임 신청자 가입 승인, 거절 메서드.
     * 승인에 대한 Boolean 타입과 정보를 받아서 승인할지 거절할지 정함.
     *
     * @param allowJoin
     * @param clubJoinInform
     * @return
     */
    @Transactional
    @Override
    public String joinClubResponse(Boolean allowJoin, ClubJoinInform clubJoinInform) {

        ClubEntity clubEntity = getClubEntity(clubJoinInform.getClubId());
        MemberEntity memberEntity = getMemberEntity(clubJoinInform.getMemberId());
        MyClubEntity myClubEntity = getMyClubEntity(clubEntity, memberEntity);
        ClubWaitingEntity clubWaitingEntity = getClubWaitingEntity(clubEntity, memberEntity);

        if (!allowJoin) {
            myClubRepository.delete(myClubEntity);
            clubWaitingRepository.delete(clubWaitingEntity);
            return "멤버 ID: " + clubJoinInform.getMemberId() + ", 닉네임: " + memberEntity.getNickname() + " 유저 소모임 가입 승인 거절.";
        }

        if (clubEntity.getMembersLimit() < clubEntity.getMembers() + 1) {
            throw new MemberLimitException();
        }

        MyClubEntity updatedMyClubEntity = MyClubEntity.builder()
                .myClubId(myClubEntity.getMyClubId())
                .memberId(myClubEntity.getMemberId())
                .clubId(myClubEntity.getClubId())
                .authority(2)
                .regDate(LocalDate.now())
                .build();

        ClubEntity updatedClubEntity = ClubEntity.builder()
                .clubId(clubEntity.getClubId())
                .clubName(clubEntity.getClubName())
                .townId(clubEntity.getTownId())
                .ownerId(clubEntity.getOwnerId())
                .members(clubEntity.getMembers() + 1)
                .membersLimit(clubEntity.getMembersLimit())
                .accessLimit(clubEntity.getAccessLimit())
                .needGrant(clubEntity.getNeedGrant())
                .regDate(clubEntity.getRegDate())
                .modDate(LocalDate.now())
                .build();

        myClubRepository.save(updatedMyClubEntity);
        clubRepository.save(updatedClubEntity);
        clubWaitingRepository.delete(clubWaitingEntity);

        return "멤버 ID: " + clubJoinInform.getMemberId() + ", 닉네임: " + memberEntity.getNickname() + " 유저 소모임 가입 승인.";
    }

    /**
     * 소모임 탈퇴 메서드.
     * @param clubId
     * @param memberId
     * @return
     */
    @Transactional
    @Override
    public String leaveClub(Long clubId, Long memberId) {

        ClubEntity clubEntity = getClubEntity(clubId);
        MemberEntity memberEntity = getMemberEntity(memberId);
        MyClubEntity myClubEntity = getMyClubEntity(clubEntity, memberEntity);

        myClubRepository.delete(myClubEntity);

        ClubEntity updatedClubEntity = ClubEntity.builder()
                .clubId(clubEntity.getClubId())
                .clubName(clubEntity.getClubName())
                .townId(clubEntity.getTownId())
                .ownerId(clubEntity.getOwnerId())
                .members(clubEntity.getMembers() - 1)
                .membersLimit(clubEntity.getMembersLimit())
                .accessLimit(clubEntity.getAccessLimit())
                .needGrant(clubEntity.getNeedGrant())
                .regDate(clubEntity.getRegDate())
                .modDate(LocalDate.now())
                .build();
        clubRepository.save(updatedClubEntity);

        return "소모임 ID: " + clubId + ", 소모임 이름: " + clubEntity.getClubName() + " 에서 탈퇴 완료되었습니다.";
    }

    /**
     * 소모임 정보를 수정하는 메서드
     * @param clubDto
     * @return
     */
    @Transactional
    @Override
    public ClubDto updateClubData(ClubDto clubDto) {
        ClubEntity clubEntity = getClubEntity(clubDto.getClubId());
        MemberEntity memberEntity = getMemberEntity(clubDto.getOwnerId());
        MyClubEntity myClubEntity = getMyClubEntity(clubEntity, memberEntity);
        TownEntity townEntity = townRepository.findByTownId(clubDto.getTownId());

        if (myClubEntity.getAuthority() != 1) {
            throw new NotAdminException();
        }

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
     * 소모임 멤버의 역항을 변경하는 메서드
     * @param clubId
     * @param memberId
     * @param authority
     * @return
     */
    @Transactional
    @Override
    public String setMemberRole(Long clubId, Long memberId, Integer authority) {

        ClubEntity clubEntity = getClubEntity(clubId);
        MemberEntity memberEntity =getMemberEntity(memberId);
        MyClubEntity myClubEntity = getMyClubEntity(clubEntity, memberEntity);

        MyClubEntity updatedMyClubEntity = MyClubEntity.builder()
                .myClubId(myClubEntity.getMyClubId())
                .memberId(memberEntity)
                .clubId(clubEntity)
                .authority(authority)
                .regDate(myClubEntity.getRegDate())
                .build();
        myClubRepository.save(updatedMyClubEntity);

        String role = "";
        if (authority == 1) {
            role = "관리자";
        } else {
            role = "회원";
        }

        return "멤버 Id: " + memberId + ", 닉네임: " + memberEntity.getNickname() + "유저의 권한이 " + role + "로 변경되었습니다.";
    }

    /**
     * 내 소모임 목록 보기 메서드
     * @param memberId
     * @return
     */
    @Override
    public List<ClubDto> getMyClub(Long memberId) {

        MemberEntity memberEntity = getMemberEntity(memberId);
        List<MyClubEntity> myClubEntities = myClubRepository.findByMemberId(memberEntity);
        if (myClubEntities.isEmpty()) {
            throw new NotFoundMyClubException();
        }

        List<ClubDto> clubDtos = new ArrayList<>();
        for (int i = 0; i < myClubEntities.size(); i++) {
            ClubEntity clubEntity = getClubEntity(myClubEntities.get(i).getClubId().getClubId());
            clubDtos.add(ClubEntity.entityToDto(clubEntity));
        }

        return clubDtos;
    }

    private ClubEntity getClubEntity(Long clubId) {
        return clubRepository.findByClubId(clubId)
                .orElseThrow(() -> new NotFoundClubException());
    }

    private MemberEntity getMemberEntity(Long memberId) {
        return memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NotFoundMemberException());
    }

    private MyClubEntity getMyClubEntity(ClubEntity clubEntity, MemberEntity memberEntity) {
        return myClubRepository.findByClubIdAndMemberId(clubEntity, memberEntity)
                .orElseThrow(() -> new NotFoundMyClubException());
    }

    private ClubWaitingEntity getClubWaitingEntity(ClubEntity clubEntity, MemberEntity memberEntity) {
        return clubWaitingRepository.findByClubIdAndMemberId(clubEntity, memberEntity)
                .orElseThrow(() -> new ClubJoinException());
    }

}
