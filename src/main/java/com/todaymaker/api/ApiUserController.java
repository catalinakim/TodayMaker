package com.todaymaker.api;

import com.todaymaker.domain.User;
import com.todaymaker.dto.UserDto;
import com.todaymaker.exception.ErrorResult;
import com.todaymaker.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ApiUserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto, HttpServletRequest req) {
        User loginUser = userService.login(userDto.getLoginId(), userDto.getPassword());
        if(loginUser == null){
            ErrorResult errorResult = ErrorResult.builder().msg("아이디 or 비밀번호 불일치").build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResult);
        }
        HttpSession session = req.getSession();  //session id생성
        session.setAttribute("loginId", loginUser.getLoginId());
        session.setAttribute("userId", loginUser.getId());

        return ResponseEntity.status(200).body("ok");
    }
}
