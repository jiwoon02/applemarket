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

public class JwtFilter extends OncePerRequestFilter{
	
	private final JwtUtil jwtUtil;
	
	public JwtFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}
	
	 @Override
	    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
	        Cookie[] cookies = request.getCookies();

	        if (cookies != null) {
	            for (Cookie cookie : cookies) {
	                if ("JWT".equals(cookie.getName())) {
	                    String token = cookie.getValue();

	                    if (!jwtUtil.isExpired(token)) {
	                        String userID = jwtUtil.getUserID(token);
	                        String userRole = jwtUtil.getRole(token);

	                        User user = new User();
	                        user.setUserID(userID);
	                        user.setUserPwd("temppassword");
	                        user.setUserRole(userRole);

	                        CustomUserDetails customUserDetails = new CustomUserDetails(user);
	                        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

	                        SecurityContextHolder.getContext().setAuthentication(authToken);
	                    }
	                }
	            }
	        }

	        filterChain.doFilter(request, response);
	    }
}












