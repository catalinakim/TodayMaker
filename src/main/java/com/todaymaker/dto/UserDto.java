package com.todaymaker.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserDto {

    @NotEmpty
    private String loginId;
    @NotEmpty
    private String password;
}
