package com.linkee.linkeeapi.quiz.query.service;

import com.linkee.linkeeapi.common.model.PageResponse;
import com.linkee.linkeeapi.quiz.query.dto.request.RoomMemberSearchRequest;
import com.linkee.linkeeapi.quiz.query.dto.response.*;
import com.linkee.linkeeapi.quiz.query.mapper.QuizRoomMapper;
import com.linkee.linkeeapi.quiz.query.mapper.RoomMemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizRoomQueryServiceImpl implements QuizRoomQueryService {

    private final QuizRoomMapper quizRoomMapper;
    private final RoomMemberMapper roomMemberMapper;

    //  방 목록 조회
    @Override
    public PageResponse<QuizRoomListResponseDto> findAllRooms(int page, int size) {
        int offset = page * size;
        List<QuizRoomListResponseDto> rooms = quizRoomMapper.findAllRoomsPaginated(size, offset);
        int total = quizRoomMapper.countAllRooms();
        return PageResponse.from(rooms, offset, size, total);
    }
    @Override
    public QuizRoomDetailResponseDto getRoomDetail(Long roomId, Long currentUserId) {
        // 1) 방 헤더(제목/방장)
        RoomHeaderRow header = quizRoomMapper.findRoomHeader(roomId);
        if (header == null) return null; // 존재하지 않는 방

        // 2) 멤버 목록 — 기존 라인 재사용(중복 SQL, 이중 유지보수 방지)
        RoomMemberSearchRequest request = new RoomMemberSearchRequest();
        request.setQuizRoomId(roomId); // 검색 조건: 해당 방의 모든 멤버
        List<RoomMemberResponse> rows = roomMemberMapper.selectAllRoomMember(request);

        // 3) 응답 DTO로 변환(닉네임/방장/준비상태 등 표준화)
        List<QuizRoomDetailResponseDto.MemberDto> members = rows.stream()
                .map(r -> QuizRoomDetailResponseDto.MemberDto.builder()
                        .roomMemberId(r.getRoomMemberId())
                        .memberId(r.getMemberId())
                        .memberNickname(r.getMemberNickname()) // RoomMemberResponse에 닉네임 필드가 있으면 사용
                        .ready("Y".equalsIgnoreCase(r.getIsReady()))
                        .owner(Objects.equals(r.getMemberId(), header.getOwnerId()))
                        .build())
                .collect(Collectors.toList());

        boolean isOwner = (currentUserId != null) && currentUserId.equals(header.getOwnerId());

        return QuizRoomDetailResponseDto.builder()
                .quizRoomId(header.getQuizRoomId())
                .roomTitle(header.getRoomTitle())
                .ownerId(header.getOwnerId())
                .ownerNickname(header.getOwnerNickname())
                .currentUserId(currentUserId)
                .isOwner(isOwner)
                .members(members)
                .build();
    }

    //  빠른 입장
    @Override
    public QuizRoomResponseDto findAvailableRoom() {
        return quizRoomMapper.findAvailableRoom();
    }

    //  플레이 상태
    @Override
    public PlayStateResponseDto getPlayState(Long roomId) {
        return quizRoomMapper.findPlayState(roomId);
    }

    //  결과 조회
    @Override
    public List<ResultRowResponseDto> getResultsByRoom(Long roomId, int page, int size) {
        int offset = page * size;
        return quizRoomMapper.findResultsByRoom(roomId, size, offset);
    }
}
