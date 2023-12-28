package com.walkbuddies.backend.admin.service;

import org.springframework.stereotype.Service;

@Service
public interface AdminFeedService {
  void restoreFeedComment(Long commentId);
  void restoreFeed(Long feedId);
  /**
   * 삭제글 복구
   * @param boardIdx
   */
  void restoreBoard(Long boardIdx);
  /**
   * 댓글복구
   * @param commentId
   */
  void restoreBoardComment(Long commentId);

}
