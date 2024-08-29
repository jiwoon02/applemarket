package com.apple.location.repository;
/*
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.apple.location.domain.Location;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class AppleMarketLocationTests {
/*유저에 locationID 삽입
	@Setter(onMethod_ = @Autowired)
	private UserRepository userRepository;
	 우선 로케이션ID 레파지토리로 호출.
	 
	@Test
	public void locationUpdateTest() {
	//update user set locationID = 1? where userID = 2?
		// 1. 유저 생성 및 LocationID 설정
    Optional<User> userOptional = userRepository.findById(1L); //임시로 1번 유저를 찾고
    if(userOptional.isPresent()){
    	User user = userOptional.get();
    	user.setLocationID(1165010800); // 예시 locationID, 실제 값으로 대체
    	log.info("user에 locationID 수정")
    	userRepository.save(user);
    }
    
    
	}
	
	//user서비스 작성부분
	 @Override
    public void userLocationUpdate(Long userId, Long locationID) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User locationUser = userOptional.get();
            locationUser.setLocationID(locationID);
            userRepository.save(locationUser);
        } else {
            throw new IllegalArgumentException("Invalid user ID: " + userId);
        }
    }
     @PostMapping("/updateLocation")
    public ResponseEntity<?> updateUserLocation(@RequestBody Map<String, Long> payload) {
        Long locationID = payload.get("locationID");
        // 가정: 현재 로그인한 사용자의 ID를 가져오는 방법이 이미 구현되어 있다면
        User user = userRepository.findById(/* 현재 사용자의 ID ).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        user.setLocationID(locationID);
        userRepository.save(user);

        return ResponseEntity.ok("Location updated successfully");
    }
    
    @PostMapping("/updateLocation")
public String updateUserLocation(@RequestBody Map<String, Long> payload) {
    Long userId = /* 현재 로그인된 사용자 ID를 가져오는 로직 ;
    Long locationID = payload.get("locationID");

    userService.userLocationUpdate(userId, locationID);
    return "Location updated successfully";
}
}*/
