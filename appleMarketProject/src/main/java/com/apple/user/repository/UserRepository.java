package com.apple.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.apple.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	// 특정 ID가 이미 존재하는지 확인하는 메서드
    boolean existsByUserIDIgnoreCase(String userID);
    
    // 특정 전화번호가 이미 존재하는지 확인하는 메서드
    boolean existsByUserPhone(String userPhone);
    
    // 특정 이메일이 이미 존재하는지 확인하는 메서드
    boolean existsByUserEmailIgnoreCase(String userEmail);

    // 특정 닉네임이 이미 존재하는지 확인하는 메서드
    boolean existsByUserNicknameIgnoreCase(String userNickname);
    
    //userNo를 기준으로 찾기
  	Optional<User> findByUserNo(Long userNo);
  	
    //이름과 이메일로 유저 찾기
    Optional<User> findByUserNameAndUserEmail(String userName, String userEmail);
    
    //이름, 아이디, 이메일로 유저찾기
    Optional<User> findByUserNameAndUserIDAndUserEmail(String userName, String userID, String userEmail);

    //아아디 기준 찾기
	Optional<User> findByUserID(String userID);
	
	@Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u.userNo = :userNo")
    void deleteByUserNo(@Param("userNo") Long userNo);
	
}