package com.walkbuddies.backend.club.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ClubBoardCommentResponse {
  private final int statusCode;
  private final String message;
  private final String data;

}
