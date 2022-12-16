package com.todaymaker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class UserDto {

    @NotEmpty
    private String loginId;

    @NotEmpty
    private String password;
}
