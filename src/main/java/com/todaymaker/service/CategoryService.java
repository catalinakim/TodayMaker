package com.todaymaker.service;

import com.todaymaker.domain.Category;
import com.todaymaker.domain.Todo;
import com.todaymaker.repository.CategoryJpaRepository;
import com.todaymaker.repository.CategoryRepository;
import com.todaymaker.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryJpaRepository categoryJpaRepository;
    private final CategoryRepository categoryRepository;
    private final TodoRepository todoRepository;

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

    public Category checkName(String name) {
        return categoryRepository.findByName(name);
    }

    @Transactional
    public Long deleteWithSubCategory(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        category.ifPresent(category1 -> {
            for(Todo todo : category1.getTodos()){
                todo.setCategoryNull();
            }
            //하위카테고리삭제
            for(Category child : category1.getChild()){
                child.setParentNull();
                categoryRepository.deleteById(child.getId());
            }
            categoryRepository.deleteById(id);
        });
        return id;
    }
    @Transactional
    public Long deleteWithoutSubCategory(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        category.ifPresent(category1 -> {
            for(Todo todo : category1.getTodos()){
                todo.setCategoryNull();
            }
            category1.getChild().forEach(child -> child.setParentNull());
            categoryRepository.deleteById(id);
        });
        return id;
    }
    @Transactional
    public Long editCategory(Long id, String name) {
        Optional<Category> categories = categoryRepository.findById(id);
        categories.ifPresent(category -> {
            category.setName(name);
        });
        return id;
    }
}
