package com.apple.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor{
	
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        boolean isLoggedIn = false;

        // 쿠키에서 JWT 토큰을 확인
        //존재하면 isLoggedIn true로 설정
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JWT".equals(cookie.getName())) {
                    isLoggedIn = true;
                    break;
                }
            }
        }

        // 로그인 상태를 요청 속성에 저장
        request.setAttribute("isLoggedIn", isLoggedIn);

        return true; // 다음 인터셉터나 컨트롤러로 요청을 진행
    }
}
