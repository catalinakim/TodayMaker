package com.todaymaker.dto;

import com.todaymaker.domain.Category;
import com.todaymaker.domain.User;
import lombok.*;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoDto {
    @NotEmpty
    private User user;

    private Category category;

    @NotEmpty
    private String name;
}
