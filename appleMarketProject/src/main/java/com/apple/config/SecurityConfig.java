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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
        			.requestMatchers("/product/insertForm").authenticated()
        			.anyRequest().permitAll());
		
		http
			.addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);
		
		//세션 설정
		http
       		.sessionManagement((session) -> session
       				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
			
		return http.build();
	}
}


