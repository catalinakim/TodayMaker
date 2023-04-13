package com.todaymaker.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String loginId;

    @NotBlank
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Todo> todos = new ArrayList<>();

    //@NotBlank(message = "이메일을 입력하세요.")
    @Email
    private String email;

    private char joinType;
    private boolean verify;
    private boolean isDeleted;
    private String createdAt;

    public User(String loginId, String password, String email, char joinType, boolean verify) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.joinType = joinType;
        this.verify = verify;
        this.isDeleted = false;
    }

    public User(String loginId, String password, String email) {
        System.out.println("in const");
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.isDeleted = false;
        this.joinType = 'F';
        LocalDateTime now = LocalDateTime.now();
        String formatNow = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss"));
        this.createdAt = formatNow;
    }

    public void setLoginId(String loginId) {
        System.out.println("in setLoginId");
        this.loginId = loginId;
    }

    public void initUser(){
        System.out.println("User.initUser");
        this.isDeleted = false;
        this.verify = false;
        this.joinType = 'F';
        LocalDateTime now = LocalDateTime.now();
        String formatNow = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss"));
        this.createdAt = formatNow;
    }
}
