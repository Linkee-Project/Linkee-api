package com.linkee.linkeeapi.inquiry.query.mapper;

import com.linkee.linkeeapi.inquiry.query.dto.response.InquiryResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InquiryMapper {

    List<InquiryResponseDto> findAll(@Param("offset") int offset,
                                     @Param("limit") int limit);

    List<InquiryResponseDto> findByUserId(@Param("userId") Long userId,
                                          @Param("offset") int offset,
                                          @Param("limit") int limit);


    int countAll();

    int countByUserId(@Param("userId") Long userId);
}
