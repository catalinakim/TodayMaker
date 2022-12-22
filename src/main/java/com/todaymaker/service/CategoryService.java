package com.todaymaker.service;

import com.todaymaker.domain.Category;
import com.todaymaker.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }

    public List<Category> findCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> findRootCategories() {
        return categoryRepository.findRootCategories();
    }
}
