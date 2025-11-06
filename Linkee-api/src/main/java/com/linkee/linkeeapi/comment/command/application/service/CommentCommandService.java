package com.linkee.linkeeapi.comment.command.application.service;

import com.linkee.linkeeapi.alarm_box.command.application.dto.request.AlarmBoxCreateRequest;
import com.linkee.linkeeapi.alarm_box.command.application.service.AlarmBoxCommandService;
import com.linkee.linkeeapi.alarm_template.query.dto.response.AlarmTemplateResponse;
import com.linkee.linkeeapi.alarm_template.query.service.AlarmTemplateQueryService;
import com.linkee.linkeeapi.comment.command.application.dto.request.CreateCommentRequestDto;
import com.linkee.linkeeapi.comment.command.application.dto.request.UpdateCommentRequestDto;
import com.linkee.linkeeapi.comment.command.application.dto.response.CreateCommentResponseDto;
import com.linkee.linkeeapi.comment.command.application.dto.response.UpdateCommentResponseDto;
import com.linkee.linkeeapi.comment.command.domain.aggregate.Comment;
import com.linkee.linkeeapi.comment.command.infrastructure.repository.JpaCommentRepository;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.question.command.domain.aggregate.Question;
import com.linkee.linkeeapi.question.command.infrastructure.repository.JpaQuestionRepository;
import com.linkee.linkeeapi.user.command.application.service.util.UserFinder;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentCommandService {

    private final JpaCommentRepository jpaCommentRepository;
    private final JpaQuestionRepository jpaQuestionRepository;
    private final UserFinder userFinder;

    private final AlarmBoxCommandService alarmBoxCommandService;
    private final AlarmTemplateQueryService alarmTemplateQueryService;

    //댓글 등록
    public CreateCommentResponseDto createComment(Long questionId, Long userId, CreateCommentRequestDto req) {
        Question question = jpaQuestionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 문제입니다."));
        User user = userFinder.getById(userId);

        Comment saved;
        if (req.getParentCommentId() == null) {
            Comment root = Comment.createRoot(question, user, req.getContent());
            saved = jpaCommentRepository.save(root);
        } else {
            Comment parent = jpaCommentRepository.findById(req.getParentCommentId())
                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글이 존재하지 않습니다."));
            Comment reply = Comment.createReply(question, user, parent, req.getContent());
            saved = jpaCommentRepository.save(reply);
        }

        // 알림 로직
        long templateId;
        if (saved.getParent() == null) {
            // 새 루트 댓글은 5번 템플릿
            templateId = 5L;
        } else {
            // 대댓글은 6번 템플릿
            templateId = 6L;
        }
        String alarmContent;

        try {
            ResponseEntity<AlarmTemplateResponse> responseEntity =
                    alarmTemplateQueryService.selectAlarmTemplateByAlarmTemplateId(templateId);

            AlarmTemplateResponse templateResponse = responseEntity.getBody();

            if (templateResponse == null || templateResponse.templateContent() == null) {
                throw new BusinessException(ErrorCode.INVALID_REQUEST);
            }

            alarmContent = templateResponse.templateContent();

        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }

        Set<Long> recipients = new HashSet<>();

        if (saved.getParent() == null) {
            // 3-A. 새 루트 댓글: 질문 작성자에게 알림
            Long questionOwnerId = question.getUser().getUserId();
            // 본인이 본인 글에 댓글 단 경우는 제외
            if (!questionOwnerId.equals(userId)) {
                recipients.add(questionOwnerId);
            }
        } else {
            // 3-B. 대댓글: 부모 댓글 작성자와 질문 작성자에게 알림
            Long parentCommentOwnerId = saved.getParent().getUser().getUserId();
            Long questionOwnerId = question.getUser().getUserId();

            // 본인이 본인 댓글에 대댓글 단 경우는 제외
            if (!parentCommentOwnerId.equals(userId)) {
                recipients.add(parentCommentOwnerId);
            }
            // 질문 작성자가 대댓글 단 경우, 또는 부모댓글 작성자와 질문작성자가 같은 경우는 제외
            if (!questionOwnerId.equals(userId) && !questionOwnerId.equals(parentCommentOwnerId)) {
                recipients.add(questionOwnerId);
            }
        }
        // 알림 전송
        recipients.forEach(recipientId -> {
            AlarmBoxCreateRequest alarmRequest = new AlarmBoxCreateRequest(alarmContent, recipientId);
            alarmBoxCommandService.createAlarmBox(alarmRequest);
        });
        // 알림 로직 끝

        return new CreateCommentResponseDto(saved.getCommentId());
    }

    //댓글 수정
    public UpdateCommentResponseDto updateComment(
            Long questionId, Long commentId, Long userId, UpdateCommentRequestDto request
    ) {
        Comment comment = jpaCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if (!comment.getQuestion().getQuestionId().equals(questionId)) {
            throw new IllegalArgumentException("해당 질문의 댓글이 아닙니다.");
        }
        if (!comment.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("댓글 작성자만 수정할 수 있습니다.");
        }

        comment.updateContent(request.getCommentContent());

        return UpdateCommentResponseDto.builder()
                .commentId(comment.getCommentId())
                .commentContent(comment.getCommentContent())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
    //댓글 삭제
    /*
     * [삭제 규칙]
     * 1. 원댓글 + 대댓글 있음 → 소프트 삭제 ("삭제된 댓글입니다"로 표시, DB에는 유지)
     * 2. 원댓글 + 대댓글 없음 → 완전 삭제 (DB에서 영구 제거)
     * 3. 대댓글 → 항상 완전 삭제 (부모가 소프트 삭제 상태이고 혼자 남았으면 부모도 같이 제거)
     */

    public void deleteComment(Long questionId, Long commentId, Long userId) {
        // [1. 댓글 조회]
        Comment target = jpaCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        // [2. 권한 검증]
        // 1) 이 댓글이 해당 질문에 속한 댓글인지 확인
        // 예: 질문1의 댓글을 질문2에서 삭제하려는 것 방지
        if (!target.getQuestion().getQuestionId().equals(questionId)) {
            throw new IllegalArgumentException("해당 질문의 댓글이 아닙니다.");
        }

        // 2) 댓글 작성자 본인만 삭제 가능
        if (!target.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("댓글 작성자만 삭제할 수 있습니다.");
        }


        // [3. 이미 삭제된 댓글이면 아무것도 안 함]
        // 예: "삭제된 댓글입니다" 상태에서 또 삭제 버튼 누른 경우
        if (target.getIsDeleted() == Status.Y) {
            return;
        }


        // [4. 댓글 구조 파악]
        boolean isRoot = (target.getParent() == null);  // 원댓글인지
        boolean hasChildren = target.getChildren() != null && !target.getChildren().isEmpty();  // 대댓글이 달려있는지


        // [5 .삭제 로직 실행]
        if (isRoot) {
            // case A: 원댓글 삭제
            if (hasChildren) {
                // A-1) 대댓글이 있는 경우 → 소프트 삭제
                target.softDelete();
            } else {
                // A-2) 대댓글이 없는 경우 → 완전 삭제
                jpaCommentRepository.delete(target);
            }
        } else {
            // 케이스 B: 대댓글 삭제
            Comment parent = target.getParent();  // 부모 댓글 찾기

            // B-1) 대댓글 삭제 (항상 완전 삭제)
            // orphanRemoval=true -> 부모의 children 컬렉션에서 제거만 하면 JPA가 자동으로 DB에서 삭제
            parent.removeChild(target);

            // B-2) 부모 댓글 정리 체크
            // 조건: 부모가 이미 "삭제된 댓글입니다" 상태 AND 남은 대댓글이 하나도 없음
            if (parent.getIsDeleted() == Status.Y &&
                    (parent.getChildren() == null || parent.getChildren().isEmpty())) {
                jpaCommentRepository.delete(parent);  // 부모도 DB에서 영구 제거
            }
        }
    }

}