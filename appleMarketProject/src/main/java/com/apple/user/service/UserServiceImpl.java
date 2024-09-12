package com.apple.user.service;

import java.util.Optional;
import java.util.UUID;

import com.apple.location.domain.Location;
import com.apple.location.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;

import com.apple.jwt.JwtUtil;
import com.apple.user.domain.User;
import com.apple.user.repository.UserRepository;
import com.apple.usershop.domain.Usershop;
import com.apple.usershop.service.UsershopService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private LocationRepository locationRepository;

    
    @Autowired
    private JwtUtil jwtUtil;
  
    @Autowired
    private UsershopService usershopService;
    
    @Autowired
    public UserServiceImpl(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    
//    @Override
//    //로그인 아이디 찾아서 user객체 반환 없으면 null
//    public Optional<User> findByUserID(String userID) {
//    	return userRepository.findByUserID(userID);
//    }
//    
//    @Override
//    //사용자 인증 처리
//    public boolean authenticate(String userID, String password) {
//        Optional<User> userOptional = findByUserID(userID);
//        
//        //아이디가 존재하면 user에 객체저장후 입력한 password가 일치하는지 확인
//        //일치한다면 true반환 -> 인증성공
//        //불일치 falser반환 -> 인증실패
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//            //입력된 비밀번호와 해싱된 비밀번호 비교
//            return passwordEncoder.matches(password, user.getUserPwd());
//        }
//        
//        return false;
//    }
    
    //회원가입
    @Override
    public User createUser(User user) {
    	user.setUserPwd(passwordEncoder.encode(user.getUserPwd()));
    	User savedUser = userRepository.save(user);
        return savedUser;
    }
    
 // 주어진 ID가 사용 가능한지 확인하는 메서드
 // 이미 존재하면 true, 존재하지않으면 false반환
    @Override
    public boolean isUserIDAvailable(String userID) {
        return userRepository.existsByUserIDIgnoreCase(userID);
    }
    
    // 주어진 전화번호가 사용 가능한지 확인하는 메서드
    @Override
    public boolean isUserPhoneAvailable(String userPhone) {
        return userRepository.existsByUserPhone(userPhone);
    }

    // 주어진 이메일이 사용 가능한지 확인하는 메서드
    @Override
    public boolean isUserEmailAvailable(String userEmail) { 
        return userRepository.existsByUserEmailIgnoreCase(userEmail);
    }

    // 주어진 닉네임이 사용 가능한지 확인하는 메서드
    @Override
    public boolean isUserNicknameAvailable(String userNickname) {
        return userRepository.existsByUserNicknameIgnoreCase(userNickname);
    }
    
    @Override
    // 마이페이지에서 주어진 전화번호가 사용 가능한지 확인하는 메서드
    public boolean isUserPhoneAvailableMyPage(String userPhone, String userID) {
        return userRepository.existsByUserPhoneAndUserIDNot(userPhone, userID);
    }
    
    // 마이페이지에서 주어진 닉네임이 사용 가능한지 확인하는 메서드
    @Override
    public boolean isUserNicknameAvailableMypage(String userNickname, String userId) {
        return userRepository.existsByUserNicknameIgnoreCaseAndUserIDNot(userNickname, userId);
    }
    
    @Override
    // 마이페이지에서 주어진 이메일이 사용 가능한지 확인하는 메서드
    public boolean isUserEmailAvailableMyPage(String userEmail, String userID) {
        return userRepository.existsByUserEmailIgnoreCaseAndUserIDNot(userEmail, userID);
    }

	@Override
	public String findId(User user) {
        // 데이터베이스에서 사용자를 조회하는 로직 구현
        Optional<User> findUser = userRepository.findByUserNameAndUserEmail(user.getUserName(), user.getUserEmail());
        
        //찾은 결과가 존재한다면
        if(findUser.isPresent()) {
        	return findUser.get().getUserID();	//찾은 유저 반환
        }
        else {
        	return "해당 정보로 등록된 사용자가 없습니다.";
        }
	}
	
	//비밀번호 찾기
	@Override
	public String findPwd(User user) {
		//데이터베이스에 사용자를 조회하는 로직 구현
		Optional<User> findUser = userRepository.findByUserNameAndUserIDAndUserEmail(user.getUserName(), user.getUserID(), user.getUserEmail());
		
		//찾은 결과가 존재한다면
		if(findUser.isPresent()) {
			User foundUser = findUser.get();
			String tempPwd = generateTempPassword();	//임시비밀번호 생성
			foundUser.setUserPwd(passwordEncoder.encode(tempPwd)); // 비밀번호 암호화
			userRepository.save(foundUser);
			
			sendEmail(user.getUserEmail(), tempPwd);
			
			return "임시 비밀번호가 이메일로 발송되었습니다.";
		} else {
			return "해당 정보로 등록된 사용자가 없습니다.";
		}
	}
	
	//임시 비밀번호 생성
	private String generateTempPassword() {
        // 임시 비밀번호 생성 로직 (예: 8자리 랜덤 문자열)
        return UUID.randomUUID().toString().substring(0, 8);
    }
	
	//메일 전송, 비동기처리
	@Async
	public void sendEmail(String to, String tempPwd) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("사과마켓 임시 비밀번호 발급 안내");
        message.setText("임시 비밀번호는: " + tempPwd + " 입니다. 로그인 후 비밀번호를 변경해 주세요.");
        mailSender.send(message);
    }

	//userNo로 구매자 이름 찾기
	@Override
	public String getNameByUserNo(Long userNo) {
		Optional<User> buyer = userRepository.findByUserNo(userNo);
		String buyerName;
		
		if(!buyer.isEmpty()) {
			buyerName = buyer.get().getUserName();
			return buyerName;
		}
		else {
			return null;
		}
	}

	//userNo로 구매자 전화번호 찾기
	@Override
	public String getPhoneByUserNo(Long userNo) {
		Optional<User> buyer = userRepository.findByUserNo(userNo);
		String buyerPhone;
		
		if(!buyer.isEmpty()) {
			buyerPhone = buyer.get().getUserPhone();
			return buyerPhone;
		}
		else {
			return null;
		}
	}
	

    //동네 설정하기
    @Override
    public void userLocationUpdate(Long userID, Long locationID) {
        Optional<User> userOptional = userRepository.findById(userID);
        Optional<Location> locationOptional = locationRepository.findById(locationID);

        if (userOptional.isPresent() && locationOptional.isPresent()) {
            User locationUser = userOptional.get();
            Location location = locationOptional.get();
            locationUser.setLocation(location);
            userRepository.save(locationUser);
        } else {
            throw new IllegalArgumentException("유효하지 않은 유저 아이디 / 위치 아이디 ==>" + userID + " / " + locationID);
        }
    }
    
    //위치 ID 를 가져오는 메서드
    @Override
    public Long getLocationIDByUserNo(Long userNo) {
        Optional<User> optionalUser = userRepository.findById(userNo);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return user.getLocation().getLocationID(); // Location 엔티티에서 LocationID를 가져옴
        }
        return null; // 사용자가 없거나 Location이 없을 경우
    }

	//쿠키에서 아이디 추출해서 해당 유저 정보 가져오기
	public User getUser(@CookieValue(value="JWT", required=false) String token) {
		String userID = jwtUtil.getUserID(token);
		Optional<User> userOptional = userRepository.findByUserID(userID);
		
		if(userOptional.isPresent()) {
			User user = userOptional.get();
			return user;
		}
		
		return null;
	}
	
	//쿠키에서 아이디 추출
	public String getUserID(String token) {
		String userID = jwtUtil.getUserID(token);
		return userID;
	}

	//userNo로 구매자 ID 찾기
   @Override
   public String getUserIDByUserNo(Long userNo) {
	   Optional<User> buyer = userRepository.findByUserNo(userNo);
		String buyerID;
		
		if(!buyer.isEmpty()) {
			buyerID = buyer.get().getUserID();
			return buyerID;
		}
		else {
			return null;
		}
   }

   
   //userNo로 구매자 닉네임 찾기
	@Override
	public String getUserNicknameByUserNo(Long userNo) {
		Optional<User> buyer = userRepository.findByUserNo(userNo);
		String buyerNickname;
		
		if(!buyer.isEmpty()) {
			buyerNickname = buyer.get().getUserNickname();
			return buyerNickname;
		}
		return null;
	}
	
	//쿠키에서 아이디 추출해서 해당 유저 번호 가져오기
	@Override
	public Long getUserNo(String token) {
		String userID = jwtUtil.getUserID(token);
		Optional<User> optionalUser = userRepository.findByUserID(userID);
		
		Long userNo;
		
		if(optionalUser.isPresent()) {
			userNo = optionalUser.get().getUserNo();
			return userNo;
		}
		else {
			return null;
		}
	}
   


}

















