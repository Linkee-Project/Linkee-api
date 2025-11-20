package com.linkee.linkeeapi.alarm.query.service;

import com.linkee.linkeeapi.alarm.query.mapper.AlarmBoxMapper;
import com.linkee.linkeeapi.alarm.query.dto.request.AlarmBoxSearchRequest;
import com.linkee.linkeeapi.alarm.query.dto.response.AlarmBoxResponse;
import com.linkee.linkeeapi.common.model.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmBoxQueryServiceImpl implements AlarmBoxQueryService {


    private final AlarmBoxMapper alarmBoxMapper;


    // 알람박스 목록 조회 및 검색
    @Override
    public PageResponse<AlarmBoxResponse> selectAllAlarmBox(AlarmBoxSearchRequest request) {

        int page = request.getPage() != null && request.getPage() >= 0 ? request.getPage() : 0;
        int size = request.getSize() != null && request.getSize() > 0 ? request.getSize() : 10;
        int offset = page * size;
        String isChecked = request.getIsChecked();

        // Mapper에 전달할 record 생성 (offset 포함)
        AlarmBoxSearchRequest requestMapper = new AlarmBoxSearchRequest(
                request.getKeyword(),
                page,
                size,
                offset,
                isChecked
        );

        List<AlarmBoxResponse> Boxes = alarmBoxMapper.selectAllAlarmBox(requestMapper);

        int total = alarmBoxMapper.countAlarmBox(requestMapper);

        return PageResponse.from(Boxes, page, size, total);
    }


    // id 값으로 단건 조회
    @Override
    public ResponseEntity<AlarmBoxResponse> selectAlarmTemplateByAlarmBoxId(Long alarmBoxId) {

        AlarmBoxResponse alarmBox = alarmBoxMapper.selectAlarmBoxByBoxId(alarmBoxId);

        return ResponseEntity.ok(alarmBox);
    }

    // 로그인 유저의 자기 알람 조회
    @Override
    public ResponseEntity<List<AlarmBoxResponse>> selectAlarmBoxByUserId(Long userId) {
        List<AlarmBoxResponse> alarmBoxes = alarmBoxMapper.selectAlarmBoxByUserId(userId);
        if (alarmBoxes == null) {
            alarmBoxes = new ArrayList<>(); // 빈 리스트로 초기화
        }
        return ResponseEntity.ok(alarmBoxes);
    }


}
