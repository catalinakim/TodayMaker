package com.todaymaker.controller;

import com.todaymaker.domain.User;
import com.todaymaker.dto.UserDto;
import com.todaymaker.repository.UserJpaRepository;
import com.todaymaker.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //로그인 페이지
    @GetMapping("/login")
    public String login(@ModelAttribute("userDto") UserDto userDto) {
        return "user/login";
    }

    @GetMapping("/join")
    public String join(@ModelAttribute("user") User user) {
        return "user/join";
    }

    @PostMapping("/join")
    public String save(@Valid @ModelAttribute User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/join";
        }
        user.initUser();
        log.info(String.valueOf(user.getJoinType()));
        log.info(user.getCreatedAt());

        User savedUser = userService.save(user);
        log.info(Long.toString(savedUser.getId()));
        return "user/login";
    }
}
