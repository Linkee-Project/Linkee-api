package com.linkee.linkeeapi.bookmark.command.application.controller;

import com.linkee.linkeeapi.bookmark.command.application.service.BookmarkCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookmarks")
@RequiredArgsConstructor
public class BookmarkCommandController {

    private final BookmarkCommandService bookmarkCommandService;

    @PostMapping("/{userId}/{questionId}")
    public ResponseEntity<String> createBookmark(
            @PathVariable Long userId,
            @PathVariable Long questionId
    ) {
        bookmarkCommandService.createBookmark(userId, questionId);
        return ResponseEntity.ok("북마크 등록 완료");
    }

    @DeleteMapping("/{userId}/{questionId}")
    public ResponseEntity<String> deleteBookmark(
            @PathVariable Long userId,
            @PathVariable Long questionId
    ) {
        bookmarkCommandService.deleteBookmark(userId, questionId);
        return ResponseEntity.ok("북마크 삭제 완료");
    }


}
