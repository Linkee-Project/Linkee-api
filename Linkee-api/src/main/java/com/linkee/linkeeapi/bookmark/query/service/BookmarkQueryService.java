package com.linkee.linkeeapi.bookmark.query.service;

import com.linkee.linkeeapi.bookmark.query.dto.request.BookmarkSearchRequest;
import com.linkee.linkeeapi.bookmark.query.dto.response.BookmarkResponse;
import com.linkee.linkeeapi.bookmark.query.mapper.BookmarkMapper;
import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.common.model.PageResponse;
import com.linkee.linkeeapi.user.command.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkQueryService {

    private final BookmarkMapper bookmarkMapper;
    private final UserRepository userRepository;

    // 전체 북마크 조회
    public PageResponse<BookmarkResponse> selectAllBookmarks(BookmarkSearchRequest request) {
        int page = request.getPage() != null && request.getPage() > 0 ? request.getPage() : 0;
        int size = request.getSize() != null && request.getSize() > 0 ? request.getSize() : 10;
        int offset = page * size;

        BookmarkSearchRequest requestMapper = BookmarkSearchRequest.builder()
                .questionId(request.getQuestionId())
                .page(page)
                .size(size)
                .offset(offset)
                .build();

        List<BookmarkResponse> results = bookmarkMapper.selectAllBookmarks(requestMapper);
        int total = bookmarkMapper.countAllBookmarks(requestMapper);

        return PageResponse.from(results, page, size, total);
    }

    // 내 북마크 조회
    public PageResponse<BookmarkResponse> selectBookmarksByUserId(BookmarkSearchRequest request) {
        int page = request.getPage() != null && request.getPage() > 0 ? request.getPage() : 0;
        int size = request.getSize() != null && request.getSize() > 0 ? request.getSize() : 10;
        int offset = page * size;

        userRepository.findById(request.getUserId()).orElseThrow(()-> new BusinessException(ErrorCode.INVALID_USER_ID));

        BookmarkSearchRequest requestMapper = BookmarkSearchRequest.builder()
                .questionId(request.getQuestionId())
                .userId(request.getUserId())
                .page(page)
                .size(size)
                .offset(offset)
                .build();

        List<BookmarkResponse> results = bookmarkMapper.selectBookmarksByUserId(requestMapper);
        int total = bookmarkMapper.countBookmarksByUserId(requestMapper);

        return PageResponse.from(results, page, size, total);
    }
}
