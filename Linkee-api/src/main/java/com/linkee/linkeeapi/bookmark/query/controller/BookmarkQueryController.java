package com.linkee.linkeeapi.bookmark.query.controller;

import com.linkee.linkeeapi.bookmark.query.dto.request.BookmarkSearchRequest;
import com.linkee.linkeeapi.bookmark.query.dto.response.BookmarkResponse;
import com.linkee.linkeeapi.bookmark.query.service.BookmarkQueryService;
import com.linkee.linkeeapi.common.model.PageResponse;
import com.linkee.linkeeapi.common.security.model.CustomUser;
import com.linkee.linkeeapi.user.command.infrastructure.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bookmarks")
@RequiredArgsConstructor
@Tag(name = "회원", description = "회원가입, 로그인, 계정, 프로필 관련 API")
public class BookmarkQueryController {

    private final BookmarkQueryService bookmarkQueryService;
    private final UserRepository userRepository;


    @GetMapping
    public ResponseEntity<PageResponse<BookmarkResponse>> getAllBookmarks(BookmarkSearchRequest request) {
        PageResponse<BookmarkResponse> response = bookmarkQueryService.selectAllBookmarks(request);

        return ResponseEntity.ok(response);
    }

    // 내 북마크 조회
    @GetMapping("/me")
    public ResponseEntity<PageResponse<BookmarkResponse>> getBookmarksByUserId(
            @AuthenticationPrincipal CustomUser customUser,
            BookmarkSearchRequest request
    ) {
        Long userId = customUser.getUserId();

        request.setUserId(userId);

        PageResponse<BookmarkResponse> response = bookmarkQueryService.selectBookmarksByUserId(request);

        return ResponseEntity.ok(response);
    }

}