package com.linkee.linkeeapi.room_member.command.application.service;
import com.linkee.linkeeapi.room_member.command.application.dto.request.RoomMemberCreateRequest;
import com.linkee.linkeeapi.room_member.command.application.dto.response.RoomMemberCreateResponse;

/*
 * 룸 멤버 관련 CUD(Create, Update, Delete) 작업을 위한 서비스 인터페이스.
 * 비즈니스 로직을 정의합니다.
 */
public interface RoomMemberCommandService {
    /*
     * 새로운 룸 멤버를 생성합니다.(입장)
     * @param request 생성할 룸 멤버의 정보를 담은 요청 객체
     * @return 생성된 룸 멤버의 정보를 담은 응답 객체
     */
    RoomMemberCreateResponse createRoomMember(RoomMemberCreateRequest request);

    /*
     * 특정 룸 멤버의 준비 상태를 토글합니다
     * @param roomMemberId 준비 상태를 변경할 룸 멤버의 ID
     */
    void toggleReady(Long roomMemberId);

    /*
     * 룸 멤버가 스스로 방을 나갑니다. (자발적 나감)
     * @param roomMemberId 나가는 룸 멤버의 ID
     */
    void selfLeaveRoom(Long roomMemberId);

    /*
     * 방장이 룸 멤버를 강제로 내보냅니다. (강퇴)
     * @param roomMemberId 강퇴할 룸 멤버의 ID
     */
    void kickRoomMember(Long roomMemberId);
}