package com.apple.security;



import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.apple.jwt.JwtUtil;
import com.apple.user.dto.CustomUserDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
	
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    
    public LoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }
    
    @Override
    public void setFilterProcessesUrl(String filterProcessesUrl) {
    	super.setFilterProcessesUrl(filterProcessesUrl);
    }
    
    @Override
    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter("userID");  // 폼에서 userID로 전달된 값을 추출
    }
    
    @Override
    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter("userPwd");  // 폼에서 userPwd로 전달된 값을 추출
    }
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

		//클라이언트 요청에서 username, password 추출
        String userID = obtainUsername(request);
        String userPwd = obtainPassword(request);
        
        System.out.println(userID);

		//스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userID, userPwd, null);

		//token에 담은 검증을 위한 AuthenticationManager로 전달
        return authenticationManager.authenticate(authToken);
    }

	//로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
    	//getPrinciple 메소드를 호출하면 인증된 사용자 정보를 가져옴
    	CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
    	
    	String userID = customUserDetails.getUsername();
    	
    	//사용자 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
    	
        String role = auth.getAuthority();
        
        String token = jwtUtil.createJwt(userID, role, 3600000L);
        
        //헤더방식
        //HTTP 인증 방식은 RFC 7235 정의에 따라 아래 인증 헤더 형태를 가져야 함
        //예시 Authorization: Bearer 인증토큰string
//        response.addHeader("Authorization", "Bearer " + token);
//        
//        System.out.println("create token success");
//    	System.out.println("login success");
    	
        Cookie jwtCookie = jwtUtil.createCookie(token);
        response.addCookie(jwtCookie);
        
        System.out.println("create token success");
        System.out.println("login success");
    }

	//로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
    	System.out.println("login fail");
    	response.setStatus(401);
    }
}