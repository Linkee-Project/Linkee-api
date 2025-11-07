package com.linkee.linkeeapi.comment.command.application.service;

import com.linkee.linkeeapi.comment.command.application.dto.request.CreateCommentRequestDto;
import com.linkee.linkeeapi.comment.command.application.dto.request.UpdateCommentRequestDto;
import com.linkee.linkeeapi.comment.command.application.dto.response.CreateCommentResponseDto;
import com.linkee.linkeeapi.comment.command.application.dto.response.UpdateCommentResponseDto;
import com.linkee.linkeeapi.comment.command.domain.aggregate.Comment;
import com.linkee.linkeeapi.comment.command.infrastructure.repository.JpaCommentRepository;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.common.event.CommentCreatedEvent;
import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.question.command.domain.aggregate.Question;
import com.linkee.linkeeapi.question.command.infrastructure.repository.JpaQuestionRepository;
import com.linkee.linkeeapi.user.command.application.service.util.UserFinder;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class CommentCommandService {

    private final JpaCommentRepository jpaCommentRepository;
    private final JpaQuestionRepository jpaQuestionRepository;
    private final UserFinder userFinder;

    private final ApplicationEventPublisher eventPublisher;

    //댓글 등록
    public CreateCommentResponseDto createComment(Long questionId, Long userId, CreateCommentRequestDto req) {
        Question question = jpaQuestionRepository.findById(questionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.QUESTION_NOT_FOUND));
        User user = userFinder.getById(userId);

        Comment saved;
        if (req.getParentCommentId() == null) {
            Comment root = Comment.createRoot(question, user, req.getCommentContent());
            saved = jpaCommentRepository.save(root);
        } else {
            Comment parent = jpaCommentRepository.findById(req.getParentCommentId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_PARENT_NOT_FOUND));
            Comment reply = Comment.createReply(question, user, parent, req.getCommentContent());
            saved = jpaCommentRepository.save(reply);
        }

        eventPublisher.publishEvent(new CommentCreatedEvent(this, saved));

        return new CreateCommentResponseDto(saved.getCommentId());
    }

    //댓글 수정
    public UpdateCommentResponseDto updateComment(
            Long questionId, Long commentId, Long userId, UpdateCommentRequestDto request
    ) {
        Comment comment = jpaCommentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getQuestion().getQuestionId().equals(questionId)) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST,"해당 질문의 댓글이 아닙니다.");
        }
        if (!comment.getUser().getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.COMMENT_FORBIDDEN_ACCESS);
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
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        // [2. 권한 검증]
        // 1) 이 댓글이 해당 질문에 속한 댓글인지 확인
        // 예: 질문1의 댓글을 질문2에서 삭제하려는 것 방지
        if (!target.getQuestion().getQuestionId().equals(questionId)) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST,"해당 질문의 댓글이 아닙니다.");
        }

        // 2) 댓글 작성자 본인만 삭제 가능
        if (!target.getUser().getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.COMMENT_FORBIDDEN_ACCESS);
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