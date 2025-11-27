package com.linkee.linkeeapi.question.command.application.service;

import com.linkee.linkeeapi.alarm.command.application.dto.request.AlarmBoxCreateRequest;
import com.linkee.linkeeapi.alarm.command.application.service.AlarmBoxCommandService;
import com.linkee.linkeeapi.alarm.query.dto.response.AlarmTemplateResponse;
import com.linkee.linkeeapi.alarm.query.mapper.AlarmTemplateMapper;
import com.linkee.linkeeapi.common.enums.AlarmType;
import com.linkee.linkeeapi.common.sse.service.SseService;
import com.linkee.linkeeapi.question.command.domain.aggregate.Category;
import com.linkee.linkeeapi.question.command.infrastructure.repository.JpaCategoryRepository;
import com.linkee.linkeeapi.common.enums.Role;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.question.command.application.dto.request.CreateQuestionRequestDto;
import com.linkee.linkeeapi.question.command.application.dto.request.UpdateQuestionRequestDto;
import com.linkee.linkeeapi.question.command.domain.aggregate.Question;
import com.linkee.linkeeapi.question.command.infrastructure.repository.JpaQuestionRepository;
import com.linkee.linkeeapi.question.command.domain.aggregate.QuestionOption;
import com.linkee.linkeeapi.users.command.application.service.util.UserFinder;
import com.linkee.linkeeapi.users.command.domain.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class QuestionCommandServiceImpl implements QuestionCommandService {

    private final JpaQuestionRepository jpaQuestionRepository;
    private final UserFinder userFinder;
    private final JpaCategoryRepository categoryRepository;
    private final AlarmBoxCommandService alarmBoxCommandService;
    private final AlarmTemplateMapper alarmTemplateMapper;
    private final SseService sseService;

    //문제 등록
    @Override
    public void createQuestion(CreateQuestionRequestDto request,Long userId) {

        User user = userFinder.getById(userId);

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

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
    public void updateQuestion(Long questionId, UpdateQuestionRequestDto request,Long userId) {
        User user = userFinder.getById(userId);

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
    /* 관리자 - 문제 검증 변경 */
    @Override
    public void verifyQuestion(Long questionId, Long adminId) {

        // 1) 관리자 조회
        User admin = userFinder.getById(adminId);
        // 2) ROLE 검사
        if (admin.getUserRole() != Role.ADMIN) {
            throw new BusinessException(ErrorCode.FORBIDDEN_QUESTION_ACCESS);
        }

        // 3) 문제 로드
        Question q = jpaQuestionRepository.findByIdWithOptions(questionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.QUESTION_NOT_FOUND));

        // 4) 상태 검증 & 검증 처리
        q.verifyByAdmin(admin); // 내부에서 이미 검증됨이면 QUESTION_ALREADY_QUALIFIED 던짐

        // 5) 알림 처리
        User questionOwner = q.getUser();
        AlarmTemplateResponse alarmTemplate = alarmTemplateMapper.selectByTemplateCode(AlarmType.QUESTION_VERIFIED.getCode());
        if (alarmTemplate == null || alarmTemplate.templateContent() == null) {
            log.error("code: {}, message: {} (templateCode: {})",
                    ErrorCode.ALARM_TEMPLATE_NOT_FOUND.getCode(),
                    ErrorCode.ALARM_TEMPLATE_NOT_FOUND.getMessage(),
                    AlarmType.QUESTION_VERIFIED.getCode());
            // 알림 템플릿이 없어도 문제 검증은 계속 진행되어야 하므로 return하지 않음.
        } else {
            String alarmContent = alarmTemplate.templateContent()
                    .replace("{questionTitle}", q.getQuestionTitle());

            AlarmBoxCreateRequest alarmBoxCreateRequest = AlarmBoxCreateRequest.builder()
                    .alarmBoxContent(alarmContent)
                    .userId(questionOwner.getUserId())
                    .build();
            alarmBoxCommandService.createAlarmBox(alarmBoxCreateRequest);

            sseService.send(questionOwner.getUserId(), "questionVerified", alarmContent);
        }
    }
    /* 관리자 - 문제 삭제 */
    @Override
    public void adminDeleteQuestion(Long questionId, Long adminId) {

        // 1) 관리자 조회
        User admin = userFinder.getById(adminId);
        // 2) ROLE 검사
        if (admin.getUserRole() != Role.ADMIN) {
            throw new BusinessException(ErrorCode.FORBIDDEN_QUESTION_ACCESS);
        }

        Question q = jpaQuestionRepository.findByIdWithOptions(questionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.QUESTION_NOT_FOUND));

        if (q.getIsDeleted() == Status.Y) {  // isDeleted()는 예시 메서드 이름
            throw new BusinessException(ErrorCode.QUESTION_ALREADY_DELETED);
        }

        q.softDelete();

    }

}

