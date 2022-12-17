package com.todaymaker.controller;

import com.todaymaker.dto.CategoryDto;
import com.todaymaker.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/category")
    public String categoryPage(Model model) {
        CategoryDto categoryDto = new CategoryDto();
        model.addAttribute("categories", categoryDto);
        return "category/form";
    }
}
