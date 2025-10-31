package com.linkee.linkeeapi.notice.query.mapper;

import com.linkee.linkeeapi.notice.query.dto.response.NoticeDetailResponseDto;
import com.linkee.linkeeapi.notice.query.dto.response.NoticeListResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NoticeMapper {
    List<NoticeListResponseDto> findAll(@Param("offset") int offset,
                                        @Param("limit") int limit);

    int countAll();

    void increaseViewCount(@Param("noticeId") Long noticeId);

    NoticeDetailResponseDto findById(@Param("noticeId") Long noticeId);
}
