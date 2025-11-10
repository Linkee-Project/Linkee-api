package com.linkee.linkeeapi.question.query.mapper;

import com.linkee.linkeeapi.question.query.dto.response.CategoryResponseDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

        // 전체 카테고리 조회 (이름 오름차순)
        List<CategoryResponseDto> findAll();
}
