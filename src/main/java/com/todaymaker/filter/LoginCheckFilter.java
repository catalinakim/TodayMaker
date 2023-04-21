package com.todaymaker.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {

    private static final String[] whiteList = {"/", "/login", "/join", "/css/*", "/js/*", "/images/*", "/todos/tester"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = httpRequest.getRequestURI();

        try{
            log.info("로그인 체크 필터 시작 {}", requestURI);
            if (!PatternMatchUtils.simpleMatch(whiteList, requestURI)) {  //whileList가 아니면 로그인 체크
                log.info("로그인 체크 로직 실행 {}", requestURI);
                HttpSession session = httpRequest.getSession(false);
                if(session == null || session.getAttribute("userId") == null){
                    log.info("미로그인 사용자");
                    httpResponse.sendRedirect("/login");
                    return; //미로그인 유저는 서블릿/컨트롤러를 호출하지 않고 여기서 끝
                }
            }
            chain.doFilter(request, response); //다음 필터로 요청 전달
        } catch (Exception e){
            throw e;  //서블릿 필터에서 예외 터지면, WAS까지 예외 보내주기
        }
    }
}
