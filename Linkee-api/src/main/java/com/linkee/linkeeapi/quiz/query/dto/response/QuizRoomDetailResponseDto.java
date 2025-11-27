package com.linkee.linkeeapi.quiz.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizRoomDetailResponseDto {

    private Long quizRoomId;
    private String roomTitle;
    private Long ownerId;
    private String ownerNickname;
    private Long currentUserId;
    private boolean isOwner;
    private List<MemberDto> members;
    private String categoryName;
    private Integer roomQuizLimit;
    private Integer roomCapacity;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberDto {
        private Long roomMemberId;
        private Long memberId;
        private String memberNickname;
        private boolean ready;         // RoomMember.isReady == Y
        private boolean owner;         // memberId == roomOwnerId
    }
}
