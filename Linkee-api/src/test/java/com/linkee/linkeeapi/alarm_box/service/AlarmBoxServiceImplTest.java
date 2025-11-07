package com.linkee.linkeeapi.alarm_box.service;

import com.linkee.linkeeapi.alarm_box.command.application.dto.request.AlarmBoxCreateRequest;
import com.linkee.linkeeapi.alarm_box.command.application.service.AlarmBoxCommandService;
import com.linkee.linkeeapi.alarm_box.command.domain.aggregate.entity.AlarmBox;
import com.linkee.linkeeapi.alarm_box.command.infrastructure.repository.AlarmBoxRepository;
import com.linkee.linkeeapi.alarm_box.query.dto.request.AlarmBoxSearchRequest;
import com.linkee.linkeeapi.alarm_box.query.dto.response.AlarmBoxResponse;
import com.linkee.linkeeapi.alarm_box.query.service.AlarmBoxQueryService;
import com.linkee.linkeeapi.common.enums.Role;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.common.model.PageResponse;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import com.linkee.linkeeapi.user.command.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AlarmBoxServiceImplTest {

    @Autowired
    private AlarmBoxQueryService queryService;
    @Autowired
    private AlarmBoxCommandService service;

    @Autowired
    private AlarmBoxRepository repository;
    @Autowired
    private UserRepository userRepository;


    @Test
    @DisplayName("알람박스 전체 조회 및 검색")
    void selectAllAlarmBox() {

        User user1 = User.createNormalUser("user01", "pass01", "배짱이");
        User user2 = User.createNormalUser("user02", "pass02", "배짱이2");
        repository.save(new AlarmBox(null, "알람내용1", Status.N, user1));
        repository.save(new AlarmBox(null, "알람내용2", Status.N, user2));
        repository.save(new AlarmBox(null, "확인한 알람내용3", Status.Y, user1));

        AlarmBoxSearchRequest request = new AlarmBoxSearchRequest(null, 0, 10, 0, null);
        AlarmBoxSearchRequest request1 = new AlarmBoxSearchRequest("확인", 0, 10, 0, null);
        AlarmBoxSearchRequest request2 = new AlarmBoxSearchRequest(null, 0, 10, 0, "Y");

        PageResponse<AlarmBoxResponse> alarmBoxResponsePageResponse = queryService.selectAllAlarmBox(request);
        PageResponse<AlarmBoxResponse> alarmBoxResponsePageResponse1 = queryService.selectAllAlarmBox(request1);
        PageResponse<AlarmBoxResponse> alarmBoxResponsePageResponse2 = queryService.selectAllAlarmBox(request2);

        assertThat(alarmBoxResponsePageResponse.getTotalElements()).isEqualTo(3);
        assertThat(alarmBoxResponsePageResponse1.getContent().get(0).getAlarmBoxContent()).isEqualTo("확인한 알람내용3");
        assertThat(alarmBoxResponsePageResponse2.getContent().size()).isEqualTo(1);

    }


    @Test
    @DisplayName("boxId 로 단건 조회")
    void selectById() {
        User user1 = User.createNormalUser("user01", "pass01", "배짱이");
        User user2 = User.createNormalUser("user02", "pass02", "배짱이2");
        repository.save(new AlarmBox(null, "알람내용1", Status.N, user1));
        repository.save(new AlarmBox(null, "알람내용2", Status.N, user2));
        repository.save(new AlarmBox(null, "확인한 알람내용3", Status.Y, user1));

        ResponseEntity<AlarmBoxResponse> alarmBox = queryService.selectAlarmTemplateByAlarmBoxId(1L);

        assertThat(alarmBox.getBody().getAlarmBoxContent()).isEqualTo("알람내용1");

    }


    @Test
    @DisplayName("알람 박스 생성")
    void saveAlarmTemplate() {
        String content = "알람 컨텐트 11";

        User user1 = User.createNormalUser("user01", "pass01", "배짱이");
        userRepository.save(user1);


        AlarmBoxCreateRequest request = new AlarmBoxCreateRequest(content, user1.getUserId());

        service.createAlarmBox(request);

        // Repository로 실제 DB에 저장되었는지 확인
        AlarmBox saved = repository.findAll().get(0);

        assertThat(saved.getAlarmBoxContent()).isEqualTo(content);
    }


    @Test
    @DisplayName("알림 확인 체크")
    void checkAlarm(){
        String content = "알람 컨텐트 11";
        User user1 = User.createNormalUser("user01", "pass01", "배짱이");
        userRepository.save(user1);
        AlarmBoxCreateRequest request = new AlarmBoxCreateRequest(content, user1.getUserId());
        service.createAlarmBox(request);

        AlarmBox alarmBox = repository.findAll().get(0);
        alarmBox.checkedAlarm();

        assertThat(alarmBox.getIsChecked()).isEqualTo(Status.Y);

    }

    @Test
    @DisplayName("알람박스 삭제")
    void deleteAlarm(){
        String content = "알람 컨텐트 11";
        User user1 = User.createNormalUser("user01", "pass01", "배짱이");
        userRepository.save(user1);
        AlarmBoxCreateRequest request = new AlarmBoxCreateRequest(content, 1L);
        service.createAlarmBox(request);

        AlarmBox alarmBox = repository.findAll().get(0);
        service.deleteAlarmBoxById(alarmBox.getAlarmBoxId());

        assertThat(repository.findAll().size()).isEqualTo(0);
    }
}