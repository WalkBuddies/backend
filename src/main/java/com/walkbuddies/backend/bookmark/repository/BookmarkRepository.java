package com.walkbuddies.backend.bookmark.repository;

import com.walkbuddies.backend.bookmark.domain.BookmarkEntity;
import com.walkbuddies.backend.club.domain.TownEntity;
import com.walkbuddies.backend.member.domain.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Long> {
    Optional<BookmarkEntity> findByTownIdAndMemberId(TownEntity townEntity, MemberEntity memberEntity);

    Optional<List<BookmarkEntity>> findByMemberId(MemberEntity memberEntity);

    Optional<BookmarkEntity> findByBookmarkId(Long bookmarkId);
}
