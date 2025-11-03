package com.linkee.linkeeapi.category.query.service;

import com.linkee.linkeeapi.category.query.dto.response.CategoryResponse;
import com.linkee.linkeeapi.category.query.mapper.CategoryMapper;
import jakarta.persistence.Cacheable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryMapper categoryMapper;

    public List<CategoryResponse> getAll() {
        return categoryMapper.findAll();
    }
}
