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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/category")
    public String categoryPage(@SessionAttribute(name="userId", required = false) Long userId, Model model) {
        //상위 카테고리 목록 전달(parentId=null)
        //1차 개발목표: 최상단 카테고리만 전달(depth:1)
        //List<Category> categories = categoryService.findCategories();
        List<Category> categories = categoryService.findRootCategories(userId);
        model.addAttribute("categories", categories);
        //빈 카테고리 Dto를 전달
        CategoryDto categoryDto = new CategoryDto();
        model.addAttribute("category", categoryDto);
        return "category/categoryForm";
    }

    @PostMapping("/category")
    public String addCategory(@ModelAttribute Category category, BindingResult bindingResult) {
        //카테고리명 중복 불가
        if(!StringUtils.hasText(category.getName())){
            bindingResult.addError(new FieldError("category","name","카테고리명 공백 불가"));
        }
        if(categoryService.checkName(category.getName()) != null){
            bindingResult.addError(new FieldError("category", "name", category.getName(),false,null,null, "카테고리명 중복"));
        }
        if(bindingResult.hasErrors()){
            return "category/categoryForm";
        }

        //상위 카테고리는 null이어도 됨, 근데 에러는 안나야함
        if (category.getParent().getId() == null) {
            //bindingResult.addError(new FieldError("category", "parent.id", "상위카테고리를 선택하지 않음"));
            category.setParentNull();
            categoryService.saveCategory(category);
        }else{
            categoryService.saveCategory(category);
        }


        return "redirect:/todos";
    }
}
