package com.linkee.linkeeapi.inquiry.service;

import com.linkee.linkeeapi.common.enums.Role;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.common.model.PageResponse;
import com.linkee.linkeeapi.inquiry.mapper.InquiryMapper;
import com.linkee.linkeeapi.inquiry.model.dto.request.CreateInquiryRequestDto;
import com.linkee.linkeeapi.inquiry.model.dto.request.UpdateInquiryAnswerRequestDto;
import com.linkee.linkeeapi.inquiry.model.dto.response.InquiryResponseDto;
import com.linkee.linkeeapi.inquiry.model.entity.Inquiry;
import com.linkee.linkeeapi.inquiry.repository.InquiryRepository;
import com.linkee.linkeeapi.user.model.entity.User;
import com.linkee.linkeeapi.user.repository.UserRepository;
import com.linkee.linkeeapi.user.service.util.UserFinder;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquiryServiceImpl implements InquiryService {

    private final InquiryRepository inquiryRepository;
    private final UserFinder userFinder;
    private final ModelMapper modelMapper;
    private final InquiryMapper inquiryMapper;

    //create - builer ver.
    @Override
    public void createInquiry(CreateInquiryRequestDto request) {
        Inquiry inquiry = Inquiry.builder()
                .inquiryTitle(request.getInquiryTitle())
                .inquiryContent(request.getInquiryContent())
                .user(userFinder.getById(request.getUserId()))
                .admin(null)
                .answerStatus(Status.N)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();


        inquiryRepository.save(inquiry);

    }

/*    //create - mapper ver.
    @Transactional
    public void createInquiry2(CreateInquiryRequestDto request) {
        Inquiry inquiry = modelMapper.map(request, Inquiry.class);
        inquiry.setUser(userFinder.getById(request.getUserId()));
        inquiryRepository.save(inquiry);
    }*/

    //READ - 전체 목록조회
    @Override
    public PageResponse<InquiryResponseDto> getInquiryList(int page, Integer size, User currentUser) {

        int pageSize = (size != null) ? size : 10;
        int offset = page * pageSize;

        List<InquiryResponseDto> inquiries;
        int total;

        if (currentUser.getUserRole() == Role.ADMIN) {
            inquiries = inquiryMapper.findAll(offset, pageSize);
            total = inquiryMapper.countAll();
        } else {
            inquiries = inquiryMapper.findByUserId(currentUser.getUserId(), offset, pageSize);
            total = inquiryMapper.countByUserId(currentUser.getUserId());
        }

        return PageResponse.from(inquiries, page, pageSize, total);
    }

    //Update -답변등록
    @Override
    @Transactional(readOnly = false)
    public void updateInquiryAnswer(UpdateInquiryAnswerRequestDto request) {
        //관리자 조회
        User adminUser = userFinder.getById(request.getAdminId());

        //관리자 권한 체크
        if (adminUser.getUserRole() != Role.ADMIN) {
            throw new IllegalStateException("관리자만 답변 등록 가능");
        }

        //문의 조회
        Inquiry inquiry = inquiryRepository.findById(request.getInquiryId())
                .orElseThrow(() -> new IllegalArgumentException("해당 문의를 찾을 수 없음"));

        //답변 등록
        //mapper 사용 x (mapper로 들어갈게 answerContent 뿐임)
        inquiry.setAnswerContent(request.getAnswerContent());
        inquiry.setAnswerStatus(Status.Y);
        inquiry.setAdmin(adminUser);
        inquiry.setUpdatedAt(LocalDateTime.now());

        inquiryRepository.save(inquiry);
    }
}
