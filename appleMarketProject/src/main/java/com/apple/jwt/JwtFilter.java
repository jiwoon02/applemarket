package com.apple.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.apple.user.domain.User;
import com.apple.user.dto.CustomUserDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// JWT 토큰을 기반으로 사용자 인증을 처리하는 필터
// 스프링 시큐리티에서 요청이 들어올 때마다 실행되며 JWT 토큰을 파싱
// 유효한 경우 사용자 정보를 SecurityContext에 설정
public class JwtFilter extends OncePerRequestFilter{
	
	//토큰 검증 및 사용자 정보 추출에 사용
	private final JwtUtil jwtUtil;
	
	public JwtFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}
	
	@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //요청에서 쿠키배열을 가져옴
		Cookie[] cookies = request.getCookies();
		
		//쿠키가있으면 쿠키를 순회해서 이름이 JWT인 쿠키에서 값(토큰)을 가져옴
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JWT".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    
                    //토큰이 만료전인경우
                    if (!jwtUtil.isExpired(token)) {
                    	//토큰에서 사용자 id,role 추출
                        String userID = jwtUtil.getUserID(token);
                        String userRole = jwtUtil.getRole(token);
                        
                        //임시 user객체 생성 id,role,임시비밀번호 설정
                        User user = new User();
                        user.setUserID(userID);
                        user.setUserPwd("temppassword");
                        user.setUserRole(userRole);
                        
                        //user객체 기반으로 customUserDetails객체 생성
                        CustomUserDetails customUserDetails = new CustomUserDetails(user);
                        //인증 토큰 생성 - principal 사용자 아이디 or 사용자객체 저장
                        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
                        //SecurityContext(AUthentication객체가 저장되는 보관소)에 인증 정보 설정
                        //httpSession에 저장되어 앱 전반에 걸쳐 전역적인 참조가 가능
                        //SecurityContextHolder - SecurityContext객체를 저장하고 감싸는 wrapper 클래스
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }
        }
        
        //다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }
}












