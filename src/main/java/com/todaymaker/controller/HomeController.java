package com.todaymaker.controller;

import com.todaymaker.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.PostConstruct;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final TodoService todoService;

    //임시 초기 데이터
    @PostConstruct
    public void init(){
        log.info("init 실행");
    }

    @GetMapping("/") //ArgumentResolver
    public String homeArgumentResolver(
            //@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
            //@Login Member loginMember,
            Model model) {

        //세션에 회원 데이터가 없으면 home
        //if (loginMember == null) {
        //    return "home";
        //}

        //세션이 유지되면 로그인으로 이동
        //model.addAttribute("member", loginMember);
        return "index";
    }


}
