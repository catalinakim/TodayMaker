package com.todaymaker.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.todaymaker.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    private Long id;
    @NotEmpty
    private String name;
    @JsonIgnore
    private Category parent;

    public CategoryDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.parent = category.getParent();
    }
}
