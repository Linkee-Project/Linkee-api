package com.linkee.linkeeapi.alarm_template.service;

import static org.junit.jupiter.api.Assertions.*;

import com.linkee.linkeeapi.alarm_template.model.dto.request.AlarmTemplateCreateRequest;
import com.linkee.linkeeapi.alarm_template.model.dto.request.AlarmTemplateSearchRequest;
import com.linkee.linkeeapi.alarm_template.model.dto.response.AlarmTemplateResponse;
import com.linkee.linkeeapi.alarm_template.model.entity.AlarmTemplate;
import com.linkee.linkeeapi.alarm_template.repository.AlarmTemplateRepository;
import com.linkee.linkeeapi.common.model.PageResponse;
import org.junit.jupiter.api.AfterEach;
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
@Transactional // 각 테스트마다 독립적인 역할을한다
class AlarmTemplateServiceImplTest {

    @Autowired
    private AlarmTemplateService service;

    @Autowired
    private AlarmTemplateRepository repository;


    @Test
    @DisplayName("알람 템플릿 생성")
    void saveAlarmTemplate() {
        String content = "템플릿 테스트 내용";
        AlarmTemplateCreateRequest request = new AlarmTemplateCreateRequest(content);

        service.createAlarmTemplate(request);

        // Repository로 실제 DB에 저장되었는지 확인
        AlarmTemplate saved = repository.findAll().get(0);

        assertThat(saved.getTemplateContent()).isEqualTo(content);
    }



    @Test
    @DisplayName("알람 템플릿 조회 및 페이징 확인")
    void selectAllAlarmTemplate() {
        // given - 목록조회를 위한 더미데이터
        repository.save(new AlarmTemplate(null, "템플릿1 바나나"));
        repository.save(new AlarmTemplate(null, "템플릿2 사과"));
        repository.save(new AlarmTemplate(null, "템플릿3 오렌지"));

        AlarmTemplateSearchRequest searchRequest = new AlarmTemplateSearchRequest(null, 0, 10 ,0);
        AlarmTemplateSearchRequest searchRequest1 = new AlarmTemplateSearchRequest("사과", 0, 10 ,0);

        // when
        PageResponse<AlarmTemplateResponse> pageResponse = service.selectAllAlarmTemplate(searchRequest);
        PageResponse<AlarmTemplateResponse> pageResponse1 = service.selectAllAlarmTemplate(searchRequest1);

        // then
        assertThat(pageResponse.getTotalElements()).isEqualTo(3);
        assertThat(pageResponse.getContent().get(0).templateContent()).isEqualTo("템플릿3 오렌지");
        assertThat(pageResponse1.getContent().get(0).templateContent()).isEqualTo("템플릿2 사과");
    }



    @Test
    @DisplayName("알람템플릿 아이디값 가지고 단건 조회")
    void selectById() {
        // given
        AlarmTemplate t1 = repository.save(new AlarmTemplate(null, "가나다"));
        repository.save(new AlarmTemplate(null, "가나다라"));

        ResponseEntity<AlarmTemplateResponse> result = service.selectAlarmTemplateByAlarmTemplateId(t1.getTemplateId());

        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().templateContent()).isEqualTo("가나다");

    }


    @Test
    @DisplayName("알람템플릿 내용 변경 테스트")
    void modifyAlarmTemplateContent(){
        //given
        AlarmTemplate template = repository.save(new AlarmTemplate(null,"변경전"));
        AlarmTemplate foundTemplate = repository.findById(template.getTemplateId()).orElseThrow();
        AlarmTemplateCreateRequest request = new AlarmTemplateCreateRequest("변경후");

        service.modifyAlarmTemplateByAlarmTemplateId(foundTemplate.getTemplateId(),request);

        assertEquals("변경후", foundTemplate.getTemplateContent());

    }



}