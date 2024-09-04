package com.apple.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.apple.jwt.JwtFilter;
import com.apple.jwt.JwtUtil;
import com.apple.security.LoginFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig{
	
	private final AuthenticationConfiguration authenticationConfiguration;
	private final JwtUtil jwtUtil;
	
	public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JwtUtil jwtUtil) {
		this.authenticationConfiguration = authenticationConfiguration;
		this.jwtUtil = jwtUtil;
	}
	
	//비밀번호 암호화
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	//AuthenticationManager Bean 등록
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
	    return configuration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		//LoginFilter 인스턴스 생성 및 커스텀 로그인 URL 설정
		LoginFilter loginFilter = new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil);
		loginFilter.setFilterProcessesUrl("/user/login");
		
		http
			.csrf((auth) -> auth.disable());
		
		//Form 로그인 방식 disable
		http
        	.formLogin((auth) -> auth.disable());

		//http basic 인증 방식 disable
		http
        	.httpBasic((auth) -> auth.disable());
		
		//경로별 인가 작업
		http
        	.authorizeHttpRequests((auth) -> auth

        			.requestMatchers("/product/insertForm","/product/updateForm", "/user/locationForm", "/community/communityInserForm").hasAuthority("USER")
        			.requestMatchers("/user/loginForm").not().hasAuthority("USER")
        			.anyRequest().permitAll());
		//JWTFilter 등록
		http
			.addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
		
		//Login필터 등록
		http
			.addFilterAt(loginFilter,  UsernamePasswordAuthenticationFilter.class);
		
		//세션 설정 사용하지않음
		http
       		.sessionManagement((session) -> session
       				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		// 인증되지 않은 사용자가 접근할 때 리디렉션 처리
		 http.exceptionHandling(exception -> exception
		            .authenticationEntryPoint(authenticationEntryPoint()));  // 인증 실패 시 처리

		return http.build();
	}
	
	@Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            // 인증되지 않은 사용자가 접근할 때 리다이렉트 처리
            response.sendRedirect("/user/loginForm");
        };
    }
}


