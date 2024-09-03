package com.apple.user.dto;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.apple.user.domain.User;

//사용자 세부정보 객체
public class CustomUserDetails implements UserDetails{
	
	private final User user;
	
	public CustomUserDetails(User user) {
		this.user = user;
	}
	
	//사용자 권한 정보 반환 메소드
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		//사용자 권한을 담아 반환하기 위한 컬렉션
		Collection<GrantedAuthority> collection = new ArrayList<>();
		
		//add메소드로 새로운 GantedAuthority 객체 컬렉션 추가
		collection.add(new GrantedAuthority() {			
			@Override
			//사용자 권한 반환
			public String getAuthority() {
				return user.getUserRole(); 	// userRole 필드를 사용하여 권한 반환
			}
		});
		//권한이 추가된 컬렉션 반환
		return collection;
	}

	@Override
	public String getPassword() {
		return user.getUserPwd();	// userPwd 필드를 사용하여 비밀번호 반환
	}

	@Override
	public String getUsername() {
		return user.getUserID();	// userID 필드를 사용하여 사용자 이름 반환
	}
	
	// 계정 만료 여부를 기본값으로 true 반환
	@Override
    public boolean isAccountNonExpired() {
        return true;
    }

	// 계정 잠금 여부를 기본값으로 true 반환
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    // 자격 증명 만료 여부를 기본값으로 true 반환
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    // 계정 활성화 여부를 기본값으로 true 반환
    @Override
    public boolean isEnabled() {
        return true;
    }
	
}
