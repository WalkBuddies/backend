package com.walkbuddies.backend.feed.service;

import com.walkbuddies.backend.feed.domain.FeedEntity;
import com.walkbuddies.backend.feed.dto.FeedDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FeedService {

  FeedDto createFeed(List<Long> files, FeedDto dto);
  FeedDto getFeed(Long feedIdx);
  Page<FeedDto> feedList( Pageable pageable, Long memberId, Integer deleteYn);
  FeedDto updateFeed(List<Long> files, FeedDto dto);
  void deleteFeed(Long feedIdx);
  FeedEntity getFeedEntity(Long feedId);
}
