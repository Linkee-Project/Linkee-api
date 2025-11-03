package com.linkee.linkeeapi.bookmark.query.mapper;

import com.linkee.linkeeapi.bookmark.query.dto.request.BookmarkSearchRequest;
import com.linkee.linkeeapi.bookmark.query.dto.response.BookmarkResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookmarkMapper {

    List<BookmarkResponse> selectAllBookmarks(BookmarkSearchRequest request);
    int countAllBookmarks(BookmarkSearchRequest request);

    List<BookmarkResponse> selectBookmarksByUserId(BookmarkSearchRequest request);
    int countBookmarksByUserId(BookmarkSearchRequest request);

}
