package com.todaymaker.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//@Data
@Getter
@Setter
@Entity
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String loginId = "what";
    @NotEmpty
    private String password;
    @NotEmpty
    private String email;
    private char joinType;
    private boolean verify;
    private boolean isDeleted;
    private String createdAt;

    //public User(String loginId, String password, String email, char joinType, boolean verify) {
    //    this.loginId = loginId;
    //    this.password = password;
    //    this.email = email;
    //    this.joinType = joinType;
    //    this.verify = verify;
    //    this.out = false;
    //}

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
