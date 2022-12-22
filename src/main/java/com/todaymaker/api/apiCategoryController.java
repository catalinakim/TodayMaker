package com.todaymaker.api;

import com.todaymaker.domain.Category;
import com.todaymaker.dto.CategoryDto;
import com.todaymaker.repository.CategoryRepository;
import com.todaymaker.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@RestController
@RequiredArgsConstructor
public class apiCategoryController {

    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    @GetMapping("/categories")
    public List<CategoryDto> categoryList() {
        List<Category> orders = categoryRepository.findAll();
        List<CategoryDto> result = orders.stream()
                .map(o -> new CategoryDto(o))
                .collect(toList());
        return result;
    }

    @GetMapping("/categories/{id}/subcategories")
    public List<CategoryDto> subCategoryList(@PathVariable Long id) {
        List<Category> orders = categoryRepository.findSub(id);
        List<CategoryDto> result = orders.stream()
                .map(o -> new CategoryDto(o))
                .collect(toList());
        return result;
    }


}
