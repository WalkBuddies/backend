package com.walkbuddies.backend.service.clubservice;

import com.walkbuddies.backend.domain.clubservice.ClubEntity;
import com.walkbuddies.backend.domain.memberservice.MemberEntity;
import com.walkbuddies.backend.dto.clubservice.ClubDto;
import com.walkbuddies.backend.dto.clubservice.ClubResponse;
import com.walkbuddies.backend.repository.clubservice.ClubRepository;
import com.walkbuddies.backend.repository.memberservice.MemberRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {

    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;

    /**
     * 소모임을 생성하는 메서드.
     * @param clubDto
     * @return
     */
    @Transactional
    @Override
    public ClubResponse createClub(ClubDto clubDto) {

        Optional<ClubEntity> existingClub = clubRepository.findByClubName(clubDto.getClubName());
        if (existingClub.isPresent()) {
            throw new EntityExistsException("이미 존재하는 소모임 이름 입니다: "  + clubDto.getClubName());
        }

        MemberEntity member = memberRepository.findById(clubDto.getOwnerId())
                .orElseThrow(() -> new EntityNotFoundException("해당 회원을 찾을 수 없습니다: " + clubDto.getOwnerId()));

       if (!clubDto.getTownId().equals(member.getTownId())) {
           throw new RuntimeException("내 동네가 아닙니다.");
       }

        ClubEntity clubEntity = ClubEntity.builder()
                .clubName(clubDto.getClubName())
                .townId(member)
                .ownerId(member)
                .members(1)
                .membersLimit(clubDto.getMembersLimit())
                .accessLimit(clubDto.getAccessLimit())
                .needGrant(clubDto.getNeedGrant())
                .regDate(LocalDate.now())
                .build();

        clubRepository.save(clubEntity);

        return ClubDto.of(HttpStatus.OK.value(), "소모임 생성을 완료했습니다.", clubEntity);
    }

    /**
     * 소모임 검색 기능 메서드.
     * @param clubName
     * @return
     */
    @Override
    public List<String> searchClub(String clubName) {

        List<ClubEntity> clubEntities = clubRepository.findByClubNameContaining(clubName);
        if (clubEntities.isEmpty()) {
            throw new EntityExistsException("검색어에 해당하는 소모임이 없습니다.");
        }

        return clubEntities.stream()
                .map(ClubEntity::getClubName)
                .collect(Collectors.toList());
    }

}
