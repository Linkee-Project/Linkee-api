package com.linkee.linkeeapi.question.query.service;

import com.linkee.linkeeapi.question.query.dto.response.CategoryResponseDto;
import com.linkee.linkeeapi.question.query.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryQueryService {

    private final CategoryMapper categoryMapper;

    public List<CategoryResponseDto> getAll() {
        return categoryMapper.findAll();
    }
}
