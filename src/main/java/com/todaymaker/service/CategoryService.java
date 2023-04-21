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

    public List<Category> findCategories(Long userId) {
        //return categoryJpaRepository.findAll();
        //로그인 유저의 카테고리 리스트
        return categoryRepository.findByUserId(userId);
    }

    public List<Category> findRootCategories(Long userId) {
        //return categoryJpaRepository.findRootCategories();
        //로그인 유저의 루트 카테고리
        return categoryRepository.findByUserIdAndParentIsNull(userId);
    }

    public Category checkName(String name) {
        return categoryRepository.findByName(name);
    }

    @Transactional
    public Long deleteWithSubCategory(Long id) { //상위/하위 구분X, 하위있으면 하위삭제, 할일 데이터 유지
        Optional<Category> categories = categoryRepository.findById(id);
        categories.ifPresent(category -> {
            for(Todo todo : category.getTodos()){
                todo.setCategoryNull();
            }
            //하위카테고리삭제
            for(Category child : category.getChild()){
                for(Todo todo : child.getTodos()){//하위카테고리에 할일이 있으면
                    todo.setCategoryNull();
                }
                child.setParentNull();
                categoryRepository.deleteById(child.getId());
            }
            //상위카테고리가 있으면 null처리 후
            if(category.getParent() != null){
                category.setParentNull();
            }
            categoryRepository.deleteById(id);
        });
        return id;
    }
    @Transactional
    public Long deleteWithoutSubCategory(Long id) { //상위/하위 구분X, 하위있으면 null처리 후 데이터 유지, 할일 데이터 유지
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

    public boolean hasSubCategory(Long id) {
        Long num = categoryRepository.countByParentId(id);
        return num > 0 ? true : false;
    }

    @Transactional
    public Long deleteCategory(Long id) {  //상위/하위 구분필요X(하위있으면 삭제막음), 할일 데이터 유지
        Category category = categoryRepository.findById(id).orElse(null);
        if(category != null){
            for (Todo todo : category.getTodos()) {
                todo.setCategoryNull();
            }
            categoryRepository.deleteById(id);
        }
        return id;
    }
}
