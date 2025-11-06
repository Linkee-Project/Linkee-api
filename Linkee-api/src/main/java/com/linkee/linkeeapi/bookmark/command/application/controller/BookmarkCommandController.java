package com.linkee.linkeeapi.bookmark.command.application.controller;

import com.linkee.linkeeapi.bookmark.command.application.service.BookmarkCommandService;
import com.linkee.linkeeapi.common.security.model.CustomUser;
import com.linkee.linkeeapi.user.command.infrastructure.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookmarks")
@RequiredArgsConstructor
@Tag(name = "회원", description = "회원가입, 로그인, 계정, 프로필 관련 API")
public class BookmarkCommandController {

    private final BookmarkCommandService bookmarkCommandService;
    private final UserRepository userRepository;

    @PostMapping("/{questionId}")
    public ResponseEntity<String> createBookmark(
            @AuthenticationPrincipal CustomUser customUser,
            @PathVariable Long questionId
    ) {

        Long userId = customUser.getUserId(); // id 값 받기가능
        String Email = customUser.getUsername(); // 등록해놓은 username -> userEmail 값도 받아오기 가능

        bookmarkCommandService.createBookmark(userId, questionId);
        return ResponseEntity.ok("북마크 등록 완료");
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity<String> deleteBookmark(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long questionId
    ) {
        Long userId = userRepository.findByUserEmail(userDetails.getUsername()).orElseThrow().getUserId();
        bookmarkCommandService.deleteBookmark(userId, questionId);
        return ResponseEntity.ok("북마크 삭제 완료");
    }


}
