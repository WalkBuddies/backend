package com.walkbuddies.backend.service.clubservice;

import com.walkbuddies.backend.domain.clubservice.ClubEntity;
import com.walkbuddies.backend.dto.clubservicec.ClubDto;
import com.walkbuddies.backend.dto.clubservicec.ClubResponse;
import com.walkbuddies.backend.repository.clubservice.ClubRepository;
import com.walkbuddies.backend.type.ClubRole;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {

    private final ClubRepository clubRepository;

    @Transactional
    @Override
    public ClubResponse createClub(ClubDto clubDto) {

        Optional<ClubEntity> existingClub = clubRepository.findByClubName(clubDto.getClubName());
        if (existingClub.isPresent()) {
            throw new EntityExistsException("이미 존재하는 소모임 이름 입니다: "  + clubDto.getClubName());
        }

        ClubEntity clubEntity = ClubEntity.builder()
                .clubName(clubDto.getClubName())
                .townId(clubDto.getTownId())
                .memberId(clubDto.getMemberId())
                .members(1)
                .membersLimit(clubDto.getMembersLimit())
                .accessLimit(clubDto.getAccessLimit())
                .needGrant(clubDto.getNeedGrant())
                .regDate(LocalDate.now())
                .build();

        clubRepository.save(clubEntity);

        return ClubDto.of(HttpStatus.OK.value(), "소모임 생성을 완료했습니다.", clubEntity);
    }
}
