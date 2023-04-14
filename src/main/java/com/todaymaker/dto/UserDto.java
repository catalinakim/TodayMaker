package com.todaymaker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class UserDto {

    @NotBlank
    private String loginId;

    @NotBlank
    private String password;
}
