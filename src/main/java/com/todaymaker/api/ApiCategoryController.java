package com.todaymaker.api;

import com.todaymaker.domain.Category;
import com.todaymaker.dto.CategoryDto;
import com.todaymaker.repository.CategoryJpaRepository;
import com.todaymaker.service.CategoryService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ApiCategoryController {

    private final CategoryService categoryService;
    private final CategoryJpaRepository categoryJpaRepository;

    @GetMapping("/categories")
    public List<CategoryDto> categoryList() {
        List<Category> orders = categoryJpaRepository.findAll();
        List<CategoryDto> result = orders.stream()
                .map(o -> new CategoryDto(o))
                .collect(toList());
        return result;
    }

    @GetMapping("/categories/{id}/subcategories")
    public List<CategoryDto> subCategoryList(@PathVariable Long id) {
        List<Category> subCateList = categoryJpaRepository.findSub(id);
        List<CategoryDto> result = subCateList.stream()
                .map(c -> new CategoryDto(c))
                .collect(toList());
        return result;
    }

    @PutMapping("/categories/{id}")
    public Long editCategory(@PathVariable Long id, @RequestBody EditCateDto dto){
        Long editId = categoryService.editCategory(id, dto.getName());
        return editId;
    }

    @DeleteMapping("/categories/{id}")
    public Long delCategory(@PathVariable Long id){
        Long delId = categoryService.deleteWithSubCategory(id);
        return delId;
    }

    @Data
    static class EditCateDto{
        private String name;
    }


}
