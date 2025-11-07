package com.linkee.linkeeapi.question.command.application.service;

import com.linkee.linkeeapi.category.command.aggregate.Category;
import com.linkee.linkeeapi.category.command.infrastructure.repository.CategoryRepository;
import com.linkee.linkeeapi.common.enums.Role;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.common.event.QuestionVerifiedEvent;
import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.question.command.application.dto.request.CreateQuestionRequestDto;
import com.linkee.linkeeapi.question.command.application.dto.request.UpdateQuestionRequestDto;
import com.linkee.linkeeapi.question.command.application.dto.request.VerifyQuestionRequestDto;
import com.linkee.linkeeapi.question.command.domain.aggregate.Question;
import com.linkee.linkeeapi.question.command.infrastructure.repository.JpaQuestionRepository;
import com.linkee.linkeeapi.question_option.command.domain.aggregate.QuestionOption;
import com.linkee.linkeeapi.user.command.application.service.util.UserFinder;
import com.linkee.linkeeapi.user.command.domain.entity.User;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher eventPublisher;

    //문제 등록
    @Override
    public void createQuestion(CreateQuestionRequestDto request) {

        if (request.getUserId() == null) {
            throw new BusinessException(ErrorCode.INVALID_USER_ID);
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        // Question 생성
        Question question = Question.builder()
                .category(category)
                .questionTitle(request.getQuestionTitle())
                .questionQuestion(request.getQuestionQuestion())
                .questionAnswer(request.getQuestionAnswer())
                .user(userFinder.getById(request.getUserId()))
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
                .orElseThrow(() -> new BusinessException(ErrorCode.QUESTION_NOT_FOUND));

        //문제 수정 권한 검증
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
                .orElseThrow(() -> new BusinessException(ErrorCode.QUESTION_NOT_FOUND));
        //문제 삭제 권한 검증
        question.assertDeletableBy(user, question.getUser().getUserId());
        question.softDelete();
    }
    //문제 검증 변경 (관리자)
    public void verifyQuestion(Long questionId, VerifyQuestionRequestDto request) {

        // 1) 관리자 조회
        User adminUser = userFinder.getById(request.getAdminId());
        // 2) ROLE 검사
        if (adminUser.getUserRole() != Role.ADMIN) {
            throw new BusinessException(ErrorCode.FORBIDDEN_QUESTION_ACCESS);
        }

        // 3) 문제 로드
        Question q = jpaQuestionRepository.findByIdWithOptions(questionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.QUESTION_NOT_FOUND));

        // 4) 상태 검증 & 검증 처리
        q.verifyByAdmin(adminUser); // 내부에서 이미 검증됨이면 QUESTION_ALREADY_QUALIFIED 던짐

        // 알림 이벤트
        eventPublisher.publishEvent(new QuestionVerifiedEvent(this, q));

    }

    }

