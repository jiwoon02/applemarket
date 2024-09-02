package com.apple.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.apple.user.domain.User;
import com.apple.user.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	//로그인 폼
	@GetMapping("/user/loginForm")
	public String loginForm() {
		return "user/loginForm";
	}
	
	@PostMapping("/user/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
        // JWT 쿠키를 삭제하는 로직
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JWT".equals(cookie.getName())) {
                    // 쿠키를 삭제하려면 동일한 이름과 경로로 만료 시간을 0으로 설정하여 덮어씀
                    cookie.setValue(null);
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    break;
                }
            }
        }

        return "client/main";
    }
	
	//회원가입 폼
	@GetMapping("/user/joinForm")
	public String joinForm(Model model) {
		model.addAttribute("user", new User());
		return "user/joinForm";
	}
	
	// 회원가입 처리
    @PostMapping("/user/join")
    public String createUser(@ModelAttribute User user) {
        userService.createUser(user);
        return "redirect:joinSuccess"; // 회원가입 성공알림페이지로 리다이렉트
    }
    
    //회원가입 성공 페이지
    @GetMapping("/user/joinSuccess")
    public String joinSuccess() {
        return "user/joinSuccess";
    }
    
    //아이디/비밀번호 찾기 선택
	@GetMapping("/user/findOption")
	public String findOption() {
		return "user/findOption";
	}
	
	//아이디 찾기 폼
	@GetMapping("/user/findIdForm")
	public String findIdForm(Model model) {
		model.addAttribute("user", new User());
		return "user/findIdForm";
	}
	
	//아이디 찾기 처리
	@PostMapping("/user/findId")
	public String findId(@ModelAttribute User user, Model model) {
		String findResult = userService.findId(user);
		
		if(findResult.equals("해당 정보로 등록된 사용자가 없습니다.")) {
			model.addAttribute("errorMessage",findResult);
			return "user/findIdForm";
		}else {
			model.addAttribute("userId", findResult);
			return "user/findIdSuccess";
		}
	}
	
	//아이디 찾기 성공 페이지
	@GetMapping("/user/findIdSuccess")
	public String findIdSuccess() {
	    return "user/findIdSuccess";
	}
	
	//비밀번호 찾기 폼
	@GetMapping("/user/findPwdForm")
	public String findPwdForm(Model model) {
		model.addAttribute("user", new User());
		return "user/findPwdForm";
	}
	
	// 비밀번호 찾기 처리
    @PostMapping("/user/findPwd")
    public String findPassword(@ModelAttribute User user, Model model) {
        String findResult = userService.findPwd(user);
        
        if(findResult.equals("해당 정보로 등록된 사용자가 없습니다.")) {
            model.addAttribute("errorMessage", findResult);
            return "user/findPwdForm";
        } else {
            model.addAttribute("successMessage", findResult);
            return "user/findPwdSuccess";
        }
    }
    
	// 비밀번호 찾기 성공 페이지
    @GetMapping("/user/findPwdSuccess")
    public String findPwdSuccess() {
        return "user/findPwdSuccess";
    }

    // 이미 존재하면 true, 존재하지않으면 false반환
    // AJAX 요청을 처리하여 특정 ID의 중복 여부를 확인
    @GetMapping("/checkUserID")
    @ResponseBody
    public boolean checkUserID(@RequestParam("userID") String userID) {
        return userService.isUserIDAvailable(userID);
    }
    
    // AJAX 요청을 처리하여 특정 전화번호의 중복 여부를 확인
    @GetMapping("/checkPhone")
    @ResponseBody
    public boolean checkPhone(@RequestParam("userPhone") String userPhone) {
        return userService.isUserPhoneAvailable(userPhone);
    }
    
    // AJAX 요청을 처리하여 특정 이메일의 중복 여부를 확인
    @GetMapping("/checkEmail")
    @ResponseBody
    public boolean checkEmail(@RequestParam("userEmail") String userEmail) {
        return userService.isUserEmailAvailable(userEmail);
    }
    
    // AJAX 요청을 처리하여 특정 닉네임의 중복 여부를 확인
    @GetMapping("/checkNickname")
    @ResponseBody
    public boolean checkNickname(@RequestParam("userNickname") String userNickname) {
        return userService.isUserNicknameAvailable(userNickname);
    }
}


















