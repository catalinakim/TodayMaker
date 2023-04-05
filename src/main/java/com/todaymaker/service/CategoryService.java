package com.todaymaker.service;

import com.todaymaker.domain.Category;
import com.todaymaker.repository.CategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryJpaRepository categoryJpaRepository;

    @Transactional
    public void saveCategory(Category category) {
        categoryJpaRepository.save(category);
    }

    public List<Category> findCategories() {
        return categoryJpaRepository.findAll();
    }

    public List<Category> findRootCategories() {
        return categoryJpaRepository.findRootCategories();
    }
}
