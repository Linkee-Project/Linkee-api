package com.linkee.linkeeapi.comment.query.mapper;

import com.linkee.linkeeapi.comment.query.dto.CommentListResponseDto;
import com.linkee.linkeeapi.common.enums.Status;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {

    /** 문제별 전체 댓글(부모+자식), 작성자 포함 */
    List<CommentListResponseDto> findAllByQuestionIdWithUser(
            @Param("questionId") Long questionId,
            @Param("status") Status status);

    /** 특정 부모 댓글의 자식(대댓글)만 */
    List<CommentListResponseDto> findChildCommentsByParentId(
            @Param("parentCommentId") Long parentCommentId,
            @Param("status") Status status);
}
