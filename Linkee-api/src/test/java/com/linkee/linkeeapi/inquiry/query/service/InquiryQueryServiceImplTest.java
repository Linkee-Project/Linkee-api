package com.linkee.linkeeapi.inquiry.query.service;

import com.linkee.linkeeapi.common.enums.Role;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.model.PageResponse;
import com.linkee.linkeeapi.inquiry.command.domain.aggregate.Inquiry;
import com.linkee.linkeeapi.inquiry.command.infrastructure.repository.JpaInquiryRepository;
import com.linkee.linkeeapi.inquiry.query.dto.response.InquiryResponseDto;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import com.linkee.linkeeapi.user.command.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class InquiryQueryServiceImplTest {

    @Autowired
    private InquiryQueryService inquiryQueryService;

    @Autowired
    private JpaInquiryRepository inquiryRepository;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User admin;

    @BeforeEach
    void setup() {
        user1 = User.createNormalUser("user01", "pass01", "배짱이");
        admin = User.createAdminUser("admin01", "adminpass", "관리자1");

        userRepository.save(user1);
        userRepository.save(admin);

        // 유저가 작성한 문의 3개
        for (int i = 1; i <= 3; i++) {
            Inquiry inquiry = Inquiry.builder()
                    .inquiryTitle("문의제목" + i)
                    .inquiryContent("문의내용" + i)
                    .user(user1)
                    .answerStatus(Status.N)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            inquiryRepository.save(inquiry);
        }

        // 다른 유저용 데이터 (테스트용)
        User anotherUser = User.createNormalUser("user02", "pass02", "개미");
        userRepository.save(anotherUser);

        Inquiry inquiry = Inquiry.builder()
                .inquiryTitle("다른 유저 문의")
                .inquiryContent("조회 제한 테스트용")
                .user(anotherUser)
                .answerStatus(Status.N)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        inquiryRepository.save(inquiry);
    }

    @Test
    @DisplayName("Inq_Test_009 일반 사용자는 자신의 문의글만 조회한다")
    void getInquiryList_User_Success() {
        // when
        PageResponse<InquiryResponseDto> response = inquiryQueryService.getInquiryList(0, 10, user1);

        // then
        assertThat(response.getContent()).hasSize(3);
        assertThat(response.getContent().get(0).getUserId()).isEqualTo(user1.getUserId());
        assertThat(response.getTotalElements()).isEqualTo(3);
    }

    @Test
    @DisplayName("Inq_Test_008 관리자는 전체 문의글을 조회할 수 있다")
    void getInquiryList_Admin_Success() {
        // when
        PageResponse<InquiryResponseDto> response = inquiryQueryService.getInquiryList(0, 10, admin);

        // then
        assertThat(response.getContent().size()).isGreaterThanOrEqualTo(4);
        assertThat(response.getTotalElements()).isEqualTo(4);
    }

    @Test
    @DisplayName("Inq_Test_010 페이지네이션 테스트 - 페이지 크기 제한 적용")
    void getInquiryList_Pagination() {
        // when (pageSize=2로 설정)
        PageResponse<InquiryResponseDto> response = inquiryQueryService.getInquiryList(0, 2, admin);

        assertThat(response.getContent()).hasSize(2); // 페이지 사이즈
        assertThat(response.getTotalElements()).isEqualTo(4); // 전체 데이터
        assertThat(response.getCurrentPage()).isEqualTo(0);

    }

    @Test
    @DisplayName("잘못된 사용자로 요청 시 BusinessException 발생")
    void getInquiryList_Fail_InvalidUser() {
        // when / then
        assertThatThrownBy(() -> inquiryQueryService.getInquiryList(0, 10, null))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("해당 사용자가 없습니다");

    }
}
