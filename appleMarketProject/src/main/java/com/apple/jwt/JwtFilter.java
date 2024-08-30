package com.apple.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtFilter extends OncePerRequestFilter{
	
	private final JwtUtil jwtUtil;
	
	public JwtFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//request에서 Authorization 헤더를 찾음
		String authorization = request.getHeader("Authorization");
		
		//Authorization 헤더 검증
		if (authorization == null || !authorization.startsWith("Bearer ")) {
			System.out.println("token null");
			filterChain.doFilter(request, response);
			
			//조건이 해당되면 메소드 종료 (필수)
			return;
		}
		
		System.out.println("authorization now");
		
		//Bearer 부분 제거 후 순수 토큰만 획득
		String token = authorization.split(" ")[1];
		
		//토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {
            System.out.println("token expired");
            filterChain.doFilter(request, response);

			//조건이 해당되면 메소드 종료 (필수)
            return;
        }
        
        //토큰에서 userID와 role 획득
        String username = jwtUtil.getUserID(token);
        String role = jwtUtil.getRole(token);
	}

}












