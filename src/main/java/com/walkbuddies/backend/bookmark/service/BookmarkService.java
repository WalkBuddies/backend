package com.walkbuddies.backend.bookmark.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.walkbuddies.backend.air.dto.AirServiceDto;
import com.walkbuddies.backend.bookmark.dto.BookmarkDto;
import com.walkbuddies.backend.bookmark.dto.BookmarkParameter;
import com.walkbuddies.backend.weather.dto.WeatherMidDto;
import com.walkbuddies.backend.weather.dto.WeatherMidResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Service
public interface BookmarkService {

    /**
     * 동네 즐겨찾기 등록 메서드
     * @param bookmarkParameter
     * @return
     */
    BookmarkDto bookmarkRedister(BookmarkParameter bookmarkParameter);

    /**
     * 내 즐겨찾기 불러오기 메서드
     * @param memberId
     * @return
     */
    List<BookmarkDto> getMyBookmark(Long memberId);

    /**
     * 즐겨찾기 삭제 메서드
     * @param bookmarkId
     * @return
     */
    BookmarkDto bookmarkDelete(Long bookmarkId);

    /**
     * 즐겨찾기 수정 메서드
     * @param bookmarkId
     * @param bookmarkName
     * @return
     */
    BookmarkDto bookmarkUpdate(Long bookmarkId, String bookmarkName);

    /**
     * 즐겨찾기 지역 중기예보 정보를 가져오는 메서드
     * @param bookmarkId
     * @return
     */
    List<WeatherMidResponse> getWeatheMidData(Long bookmarkId);

    /**
     * 즐겨 찾기 지역 미세먼지 정보를 가져오는 메서드
     * @param bookmarkId
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */

    AirServiceDto getAirData(Long bookmarkId) throws IOException, URISyntaxException;
}
