package com.linkee.linkeeapi.bookmark.command.application.service;

import com.linkee.linkeeapi.bookmark.command.domain.aggregate.entity.Bookmark;
import com.linkee.linkeeapi.bookmark.command.infrastructure.repository.BookmarkRepository;
import com.linkee.linkeeapi.chat_room.command.infrastructure.repository.JpaChatRoomRepository;
import com.linkee.linkeeapi.question.command.domain.aggregate.Question;
import com.linkee.linkeeapi.question.command.infrastructure.repository.JpaQuestionRepository;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import com.linkee.linkeeapi.user.command.infrastructure.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkCommandService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final JpaQuestionRepository jpaQuestionRepository;


    @Transactional
    public void createBookmark(Long userId, Long questionId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Question question = jpaQuestionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 문제입니다."));

        // 중복 방지
        if (bookmarkRepository.existsByUserAndQuestion(user, question)) {
            throw new IllegalStateException("이미 북마크한 문제입니다.");
        }

        Bookmark bookmark = Bookmark.builder()
                .user(user)
                .question(question)
                .build();

        bookmarkRepository.save(bookmark);
    }


    @Transactional
    public void deleteBookmark(Long userId, Long questionId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Question question = jpaQuestionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 문제입니다."));

        if (!bookmarkRepository.existsByUserAndQuestion(user, question)) {

            throw new IllegalArgumentException("해당 북마크가 존재하지 않습니다.");

        }

        bookmarkRepository.deleteByUserAndQuestion(user, question);
    }

}