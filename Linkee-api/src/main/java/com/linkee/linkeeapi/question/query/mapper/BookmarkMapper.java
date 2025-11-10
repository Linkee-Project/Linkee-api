package com.linkee.linkeeapi.question.query.mapper;

import com.linkee.linkeeapi.question.query.dto.response.BookmarkListResponseDto;
import com.linkee.linkeeapi.question.query.dto.request.BookmarkSearchRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookmarkMapper {

    List<BookmarkListResponseDto> selectAllBookmarks(BookmarkSearchRequest request);
    int countAllBookmarks(BookmarkSearchRequest request);

    List<BookmarkListResponseDto> selectBookmarksByUserId(BookmarkSearchRequest request);
    int countBookmarksByUserId(BookmarkSearchRequest request);

}
