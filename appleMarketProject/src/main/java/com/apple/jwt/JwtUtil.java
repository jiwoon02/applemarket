package com.apple.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;

@Component
public class JwtUtil {
	
	// JWT 서명을 위한 비밀 키를 저장할 변수
	private SecretKey secretKey;
	
	//secretkey를 바탕으로 secretkeyspeec객체를 만들어 hs256서명에 사용되는 비밀키 생성
	public JwtUtil(@Value("${spring.jwt.secret}")String secret) {
		this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), 
				Jwts.SIG.HS256.key().build().getAlgorithm());
		
	}
	
	//userID 클레임을 추출하는 메서드
	public String getUserID(String token) {
		return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("userID", String.class);
	}
	
	//role 클레임을 추출하는 메서드
    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    //JWT의 만료 여부를 확인하는 메서드
    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }
    
    //새로운 JWT를 생성하는 메서드
    public String createJwt(String userID, String role, Long expiredMs) {
    	return Jwts.builder()
    			.claim("userID", userID)
    			.claim("role", role)
    			.issuedAt(new Date(System.currentTimeMillis()))
    			.expiration(new Date(System.currentTimeMillis() + expiredMs)) // 만료 시간 설정
    			.signWith(secretKey)
    			.compact();
    }
    
    //쿠키 생성 메서드
    public Cookie createCookie(String token) {
    	Cookie cookie = new Cookie("JWT", token);
    	cookie.setHttpOnly(true);
    	cookie.setPath("/");
    	cookie.setMaxAge(60*60);
    	return cookie;
    }
}










