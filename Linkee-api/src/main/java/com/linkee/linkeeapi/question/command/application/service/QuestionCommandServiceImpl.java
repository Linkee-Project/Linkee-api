package com.linkee.linkeeapi.question.command.application.service;

import com.linkee.linkeeapi.category.command.aggregate.Category;
import com.linkee.linkeeapi.category.command.infrastructure.repository.CategoryRepository;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.question.command.application.dto.request.CreateQuestionRequestDto;
import com.linkee.linkeeapi.question.command.application.dto.request.UpdateQuestionRequestDto;
import com.linkee.linkeeapi.question.command.domain.aggregate.Question;
import com.linkee.linkeeapi.question.command.infrastructure.repository.JpaQuestionRepository;
import com.linkee.linkeeapi.question_option.command.domain.aggregate.QuestionOption;
import com.linkee.linkeeapi.user.command.application.service.util.UserFinder;
import com.linkee.linkeeapi.user.command.domain.entity.User;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionCommandServiceImpl implements QuestionCommandService {

    private final JpaQuestionRepository jpaQuestionRepository;
    private final UserFinder userFinder;
    private final CategoryRepository categoryRepository;

    //문제 등록
    @Override
    public void createQuestion(CreateQuestionRequestDto request) {

        User user = userFinder.getById(request.getUserId());
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

        // Question 생성
        Question question = Question.builder()
                .category(category)
                .questionTitle(request.getQuestionTitle())
                .questionQuestion(request.getQuestionQuestion())
                .questionAnswer(request.getQuestionAnswer())
                .user(user)
                .isQualified(Status.N)
                .isDeleted(Status.N)
                .questionViews(0L)
                .build();
        // Question_Option 생성
        if (request.getOptions() != null) {
            for (CreateQuestionRequestDto.OptionDto od : request.getOptions()) {
                QuestionOption option = QuestionOption.builder()
                        .optionIndex(od.getIndex())
                        .optionText(od.getText())
                        .isCorrected(od.getIndex().equals(request.getQuestionAnswer()) ? Status.Y : Status.N)
                        .build();
                question.addOption(option); // addOption 내부에서 option.setQuestion(this) 수행
            }
        }

        jpaQuestionRepository.save(question);


    }
    // 문제 수정
    @Override
    public void updateQuestion(Long questionId, UpdateQuestionRequestDto request) {
        User user = userFinder.getById(request.getUserId());

        Question question = jpaQuestionRepository.findByIdWithOptions(questionId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 문제입니다."));

        question.assertUpdatableBy(user, question.getUser().getUserId());

        // 제목 수정
        if (request.getQuestionTitle() != null) {
            question.changeTitle(request.getQuestionTitle());
        }

        // 내용 수정
        if (request.getQuestionQuestion() != null) {
            question.changeQuestion(request.getQuestionQuestion());
        }

        // 옵션 개별 수정
        if (request.getOptions() != null && !request.getOptions().isEmpty()) {
            // 기존 옵션을 optionIndex로 매핑
            Map<Integer, QuestionOption> existingOptionsMap = question.getOptions().stream()
                    .collect(Collectors.toMap(
                            QuestionOption::getOptionIndex,
                            option -> option
                    ));

            // 요청된 각 옵션 처리
            for (UpdateQuestionRequestDto.UpdateOption dto : request.getOptions()) {
                QuestionOption existingOption = existingOptionsMap.get(dto.getOptionIndex());

                if (existingOption != null) {
                    // 기존 옵션 수정
                    existingOption.updateOption(dto.getOptionIndex(), dto.getOptionText());
                } else {
                    // 새 옵션 추가
                    QuestionOption newOption = QuestionOption.builder()
                            .question(question)
                            .optionIndex(dto.getOptionIndex())
                            .optionText(dto.getOptionText())
                            .isCorrected(Status.N)
                            .build();
                    question.addOption(newOption);
                }
            }
        }

        // 정답 수정
        if (request.getQuestionAnswer() != null) {
            question.changeAnswer(request.getQuestionAnswer());
        }
    }

    // 문제 삭제
    @Override
    public void deleteQuestion(Long questionId, Long userId) {
        User user = userFinder.getById(userId);

        Question question = jpaQuestionRepository.findByIdWithOptions(questionId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 문제입니다."));

        question.assertDeletableBy(userId, question.getUser().getUserId());
        question.softDelete();
    }
    }

