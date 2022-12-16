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

    @GetMapping("/")
    public String home(Model model) {

        return "index";
    }


}
