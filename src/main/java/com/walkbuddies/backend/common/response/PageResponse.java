package com.walkbuddies.backend.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class PageResponse<Page> {
  int statusCode;
  String message;
  Page data;
}
