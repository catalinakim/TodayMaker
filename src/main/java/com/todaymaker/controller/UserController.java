package com.todaymaker.controller;

import com.todaymaker.domain.User;
import com.todaymaker.dto.UserDto;
import com.todaymaker.repository.UserJpaRepository;
import com.todaymaker.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/join")
    public String join(@ModelAttribute("user") User user) {
        return "user/join";
    }

    @PostMapping("/join")
    public String save(@Valid @ModelAttribute User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/join";
        }
        try{
            userService.duplicate(user.getLoginId());
        }catch (IllegalStateException e){
            bindingResult.addError(new FieldError("userDto", "loginId", "사용중인 ID"));
            return "user/join";
        }
        user.initUser();
        log.info(String.valueOf(user.getJoinType()));
        log.info(user.getCreatedAt());

        User savedUser = userService.save(user);
        log.info(Long.toString(savedUser.getId()));
        return "redirect:/login";
    }
    //로그인 페이지
    @GetMapping("/login")
    public String loginPage(@ModelAttribute("userDto") UserDto userDto) {
        return "user/login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("userDto") UserDto userDto, BindingResult bindingResult, HttpServletRequest req) {
        if(bindingResult.hasErrors()){
            return "user/login";
        }
        User loginUser = userService.login(userDto.getLoginId(), userDto.getPassword());
        if(loginUser == null){
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "user/login";
        }
        HttpSession session = req.getSession();  //session id생성
        session.setAttribute("loginId", loginUser.getLoginId());
        session.setAttribute("userId", loginUser.getId());

        return "redirect:/";

    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest req){
        HttpSession session = req.getSession(false);
        if(session != null){
            session.invalidate();
        }
        return "redirect:/";
    }
}
