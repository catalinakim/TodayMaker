package com.todaymaker.controller;

import com.todaymaker.domain.Category;
import com.todaymaker.dto.CategoryDto;
import com.todaymaker.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/category")
    public String categoryPage(Model model) {
        //상위 카테고리 목록 전달(parentId=null)
        List<Category> categories = categoryService.findCategories();
        model.addAttribute("categories", categories);
        //빈 카테고리 Dto를 전달
        CategoryDto categoryDto = new CategoryDto();
        model.addAttribute("category", categoryDto);
        return "category/categoryForm";
    }

    @PostMapping("/category")
    public String addCategory(@ModelAttribute Category category, BindingResult bindingResult) {
        //상위 카테고리는 null이어도 됨, 근데 에러는 안나야함
        if (category.getParent().getId() == null) {
            System.out.println("부모카테고리 null");
        }

        return "category/categoryForm";
    }
}
