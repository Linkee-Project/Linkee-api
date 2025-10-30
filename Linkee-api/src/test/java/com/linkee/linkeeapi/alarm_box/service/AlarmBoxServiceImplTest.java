package com.linkee.linkeeapi.alarm_box.service;

import com.linkee.linkeeapi.alarm_box.model.dto.request.AlarmBoxSearchRequest;
import com.linkee.linkeeapi.alarm_box.model.dto.response.AlarmBoxResponse;
import com.linkee.linkeeapi.alarm_box.model.entity.AlarmBox;
import com.linkee.linkeeapi.alarm_box.repository.AlarmBoxRepository;
import com.linkee.linkeeapi.common.enums.Role;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.common.model.PageResponse;
import com.linkee.linkeeapi.user.model.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AlarmBoxServiceImplTest {

    @Autowired
    private AlarmBoxService service;

    @Autowired
    private AlarmBoxRepository repository;


    @Test
    @DisplayName("알람박스 전체 조회 및 검색")
    void selectAllAlarmBox(){

        User user1 = new User(null,"user01","pass01","배짱이", LocalDateTime.now(),LocalDateTime.now(), Status.Y, Role.USER);
        User user2 = new User(null,"user02","pass02","배짱이2", LocalDateTime.now(),LocalDateTime.now(), Status.Y, Role.USER);
        repository.save(new AlarmBox(null,"알람내용1",Status.N,user1));
        repository.save(new AlarmBox(null,"알람내용2",Status.N,user2));
        repository.save(new AlarmBox(null,"확인한 알람내용3",Status.Y,user1));

        AlarmBoxSearchRequest request = new AlarmBoxSearchRequest(null,0,10,0,null);
        AlarmBoxSearchRequest request1 = new AlarmBoxSearchRequest("확인",0,10,0,null);
        AlarmBoxSearchRequest request2 = new AlarmBoxSearchRequest(null,0,10,0,"Y");

        PageResponse<AlarmBoxResponse> alarmBoxResponsePageResponse = service.selectAllAlarmBox(request);
        PageResponse<AlarmBoxResponse> alarmBoxResponsePageResponse1 = service.selectAllAlarmBox(request1);
        PageResponse<AlarmBoxResponse> alarmBoxResponsePageResponse2 = service.selectAllAlarmBox(request2);

        assertThat(alarmBoxResponsePageResponse.getTotalElements()).isEqualTo(3);
        assertThat(alarmBoxResponsePageResponse1.getContent().get(0).getAlarmBoxContent()).isEqualTo("확인한 알람내용3");
        assertThat(alarmBoxResponsePageResponse2.getContent().size()).isEqualTo(1);

    }


    @Test
    @DisplayName("boxId 로 단건 조회")
    void selectById(){
        User user1 = new User(null,"user01","pass01","배짱이", LocalDateTime.now(),LocalDateTime.now(), Status.Y, Role.USER);
        User user2 = new User(null,"user02","pass02","배짱이2", LocalDateTime.now(),LocalDateTime.now(), Status.Y, Role.USER);
        repository.save(new AlarmBox(null,"알람내용1",Status.N,user1));
        repository.save(new AlarmBox(null,"알람내용2",Status.N,user2));
        repository.save(new AlarmBox(null,"확인한 알람내용3",Status.Y,user1));

        ResponseEntity<AlarmBoxResponse> alarmBox
                = service.selectAlarmTemplateByAlarmBoxId(1L);

        assertThat(alarmBox.getBody().getAlarmBoxContent()).isEqualTo("알람내용1");

    }
}