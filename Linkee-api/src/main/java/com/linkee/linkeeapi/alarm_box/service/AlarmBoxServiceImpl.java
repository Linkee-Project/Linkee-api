package com.linkee.linkeeapi.alarm_box.service;

import com.linkee.linkeeapi.alarm_box.mapper.AlarmBoxMapper;
import com.linkee.linkeeapi.alarm_box.model.dto.request.AlarmBoxSearchRequest;
import com.linkee.linkeeapi.alarm_box.model.dto.response.AlarmBoxResponse;
import com.linkee.linkeeapi.alarm_box.repository.AlarmBoxRepository;
import com.linkee.linkeeapi.alarm_template.model.dto.request.AlarmTemplateSearchRequest;
import com.linkee.linkeeapi.alarm_template.model.dto.response.AlarmTemplateResponse;
import com.linkee.linkeeapi.common.model.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmBoxServiceImpl implements AlarmBoxService{

    private final AlarmBoxRepository alarmBoxRepository;
    private final AlarmBoxMapper alarmBoxMapper;


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



}
