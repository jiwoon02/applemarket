package com.apple.user.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.apple.user.domain.User;
import com.apple.user.repository.UserRepository;

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
    public void createUser(User user) {
    	user.setUserPwd(passwordEncoder.encode(user.getUserPwd()));
    	userRepository.save(user);
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
	private void sendEmail(String to, String tempPwd) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("사과마켓 임시 비밀번호 발급 안내");
        message.setText("임시 비밀번호는: " + tempPwd + " 입니다. 로그인 후 비밀번호를 변경해 주세요.");
        mailSender.send(message);
    }

	@Override
	public String getNameByUserNo(long userNo) {
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

	@Override
	public String getPhoneByUserNo(long userNo) {
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
    
}

















