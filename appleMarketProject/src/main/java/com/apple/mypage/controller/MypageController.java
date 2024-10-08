package com.apple.mypage.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.apple.jwt.JwtUtil;
import com.apple.mypage.dto.MypageReviewDTO;
import com.apple.mypage.dto.PasswordCheckDTO;
import com.apple.mypage.dto.WithdrawDTO;
import com.apple.mypage.service.MypageService;
import com.apple.product.domain.OrderProductDTO;
import com.apple.product.domain.Product;
import com.apple.user.domain.User;
import com.apple.user.repository.UserRepository;
import com.apple.usershop.domain.ItemReview;
import com.apple.usershop.domain.Usershop;
import com.apple.usershop.repository.UsershopRepository;
import com.apple.usershop.repository.UsershopReviewRepository;
import com.apple.usershop.service.UsershopService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/mypage")
public class MypageController {
	
	@Setter(onMethod_ = @Autowired)
    private MypageService mypageService;
    
    @Setter(onMethod_ = @Autowired)
    private UsershopService usershopService;
    
    @Setter(onMethod_ = @Autowired)
    private UsershopReviewRepository usershopReviewRepository;
    
    @Setter(onMethod_ = @Autowired)
    private UsershopRepository usershopRepository;
    
    @Setter(onMethod_ = @Autowired)
    private UserRepository userRepository;
    
    @Autowired
    private JwtUtil jwtUtil;  // JwtUtil을 주입
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    //마이페이지
//    @GetMapping("{userNo}")
//    public String getRecentBuyItemsByUserNo(@PathVariable Long userNo, Model model) {
//        List<Product> items = mypageService.getRecentBuyItemsByUserNo(userNo);
//        model.addAttribute("items", items);
//        return "mypage/mypage"; // 반환할 뷰의 이름
//    }
    
    @GetMapping("")
    public String getRecentBuyItemsByUserNo(@CookieValue(value="JWT", required=false) String token, Model model) {
    	
    	return "mypage/mypage"; // 반환할 뷰의 이름
    }
    
	@GetMapping("/buy")
	public String getBuyItemsByUserNo(@CookieValue(value="JWT", required=false) String token, Model model) {
		Long userNo = mypageService.getUserNo(token);
		model.addAttribute("userNo", userNo);
		
	    List<OrderProductDTO> items = mypageService.getBuyItemsByUserNo(userNo);
	    model.addAttribute("items", items);
	    
	    List<Long> productIds = mypageService.getProductIdsByUserNo(userNo);
	    model.addAttribute("productIds", productIds);	//리뷰작성한 productID
	    
	    return "mypage/mypageBuyItem"; // 반환할 뷰의 이름
	}
    
	//판매상품
    @GetMapping("sellAll")
    public String getAllItemsByUserNo(@CookieValue(value="JWT", required=false) String token, Model model) {
    	Long userNo = mypageService.getUserNo(token);
    	model.addAttribute("userNo", userNo);
    	
        List<Product> items = mypageService.getAllItemsByUserNo(userNo);

        
        model.addAttribute("items", items);
        
        return "mypage/mypageSellItem"; // 반환할 뷰의 이름
    }
    
    
    // 리뷰 작성 페이지를 보여주는 메서드 추가
    // shopId = userId
    @GetMapping("/addReview/{productName}/{productID}")
    public String showAddReviewForm(@PathVariable String productName, 
                                    @PathVariable Long productID, 
                                    @CookieValue(value="JWT", required=false) String token,
                                    Model model) {
    	Long userNo = mypageService.getUserNo(token);
		model.addAttribute("userNo", userNo);
		
		Long shopId = mypageService.getUserNoByProductID(productID);
		
		// shopId를 사용하여 userNickname을 가져옴
        String userNickname = mypageService.getUserNicknameByShopId(shopId);
        model.addAttribute("userNickname", userNickname);
        
        // 상점 평균평점 
        List<ItemReview> itemReviewList = usershopService.usershopReviewListByShopId(shopId);
        model.addAttribute("itemReviewList", itemReviewList);
        long averageStarRating = usershopService.calculateAverageStarRating(itemReviewList);
        model.addAttribute("averageStarRating", averageStarRating);
        
        // Usershop 정보 설정
        Optional<Usershop> usershopOptional = usershopRepository.findById(shopId);
        if (usershopOptional.isPresent()) {
            Usershop usershop = usershopOptional.get();
            model.addAttribute("usershop", usershop);
        }
        
        Optional<ItemReview> reviewOptional = mypageService.getReviewByUserNoAndProductId(userNo, productID);
        if (reviewOptional.isPresent()) {
            ItemReview review= reviewOptional.get();
            model.addAttribute("review", review);
        }
         
        model.addAttribute("shopId", shopId);
        model.addAttribute("productName", productName);
        model.addAttribute("productID", productID);

        return "mypage/mypageReview"; // 리뷰 작성 페이지로 이동
    }
    
    // 리뷰를 추가하는 메서드
    @PostMapping("/addReview")
    public String addReview(@ModelAttribute MypageReviewDTO reviewDto) {
    	
        mypageService.addReview(reviewDto);
        return "redirect:/mypage/buy";
    }
    
    // 리뷰 수정
    @PostMapping("/updateReview")
    public String updateReview(@ModelAttribute MypageReviewDTO reviewDto, @RequestParam("reviewNo") Long reviewNo) {
        // 서비스 메서드를 호출하여 리뷰 수정 로직 실행
        mypageService.updateReviewById(reviewNo, reviewDto);
        return "redirect:/mypage/buy"; // 수정 완료 후 구매 내역 페이지로 리다이렉트
    }
    
    // 리뷰 삭제
    @PostMapping("/deleteReview")
    public String deleteReview(@RequestParam("reviewNo") Long reviewNo) {
        // 서비스 메서드를 호출하여 리뷰 삭제 로직 실행
        mypageService.deleteReviewById(reviewNo);
        return "redirect:/mypage/buy"; // 삭제 완료 후 구매 내역 페이지로 리다이렉트
    }

    
    // 비밀번호 확인 페이지로 이동
    @GetMapping("/userPasswordCheck")
    public String checkPasswordForm(@CookieValue(value="JWT", required=false) String token, Model model) {
    	Long userNo = mypageService.getUserNo(token);
    	
        PasswordCheckDTO passwordCheckDTO = new PasswordCheckDTO();
        passwordCheckDTO.setUserNo(userNo);
        model.addAttribute("passwordCheckDTO", passwordCheckDTO);
        return "mypage/mypageUserPasswordCheck"; // 비밀번호 확인 페이지로 이동
    }
    
    @PostMapping("/modifyInfo")
    public String checkPassword(@ModelAttribute PasswordCheckDTO passwordCheckDTO, @CookieValue(value="JWT", required=false) String token, Model model) {
    	Long userNo = mypageService.getUserNo(token);
        boolean isPasswordCorrect = mypageService.checkPassword(passwordCheckDTO.getUserNo(), passwordCheckDTO.getUserPwd());
        
        if (isPasswordCorrect) {
            // 비밀번호가 일치하면 원하는 로직을 수행하고 다음 페이지로 이동
        	Optional<User> optionalUser = userRepository.findByUserNo(userNo);
        	User user = optionalUser.get();
            model.addAttribute("user", user);
            return "mypage/mypageUserInfoRetouch"; // 성공 페이지로 이동
        } else {
            // 비밀번호가 일치하지 않으면 오류 메시지와 함께 다시 입력 페이지로 이동
            model.addAttribute("error", "비밀번호가 맞지 않습니다. 다시 입력해 주세요.");
            return "mypage/mypageUserPasswordCheck"; // 비밀번호 확인 페이지로 다시 이동
        }
    }

    @PostMapping("/updateUserInfo") 
    public String updateUserInfo(@ModelAttribute("user") User updatedUser, @CookieValue(value="JWT", required=false) String token, Model model) {
    	Long userNo = mypageService.getUserNo(token);
    	
    	mypageService.updateUserInfo(userNo, updatedUser);
        return "redirect:/mypage"; // 수정 완료 후 마이페이지로 리다이렉트
    }
    
	//비밀번호 변경 페이지
    @GetMapping("/userPasswordUpdate")
    public String userPasswordUpdate(Model model){
    	model.addAttribute("user", new User());
        return "mypage/mypageUserPasswordUpdate";
    }
    
    @PostMapping("/updatePassword")
    public String updatePassword(@CookieValue(value="JWT", required=false) String token,
                                 @RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 Model model) {
        Long userNo = mypageService.getUserNo(token);
        Optional<User> optionalUser = userRepository.findByUserNo(userNo);
        
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            
            // 현재 비밀번호가 일치하는지 확인
            if (!passwordEncoder.matches(currentPassword, user.getUserPwd())) {
                model.addAttribute("errorMessage", "현재 비밀번호가 일치하지 않습니다.");
                return "mypage/mypageUserPasswordUpdate";  // 다시 비밀번호 변경 페이지로 이동
            }
            
            // 새로운 비밀번호 설정
            user.setUserPwd(passwordEncoder.encode(newPassword));
            userRepository.save(user); // 비밀번호 변경 후 저장
            
            return "redirect:/mypage";  // 성공적으로 변경 후 마이페이지로 리다이렉트
        }
        
        model.addAttribute("errorMessage", "사용자를 찾을 수 없습니다.");
        return "mypage/mypageUserPasswordUpdate";
    }
    
    @GetMapping("/withdraw")
    public String withdrawPage() {
    	return "mypage/mypageWithdraw";
    }
    
    @GetMapping("/withdrawNote")
    public String withdrawNotePage(@CookieValue(value="JWT", required=false) String token, Model model) {
    	Long userNo = mypageService.getUserNo(token);
    	model.addAttribute("userNo", userNo);
    	return "mypage/mypageWithdrawNote";
    }
    
    @GetMapping("/withdrawComment")
    public String withdrawCommentPage(@CookieValue(value="JWT", required=false) String token, Model model) {
    	Long userNo = mypageService.getUserNo(token);
    	model.addAttribute("userNo", userNo);
    	return "mypage/mypageWithdrawComment";
    }
    
    // User 객체를 가져오는 메서드
    public User getUser(@CookieValue(value="JWT", required=false) String token) {
        if (token != null) {
            String userID = jwtUtil.getUserID(token);
            Optional<User> userOptional = userRepository.findByUserID(userID);
        
            return userOptional.orElse(null);
        }
        return null;
    }
    
    @PostMapping("/userDelete")
    public String deleteUser(@CookieValue(value="JWT", required=false) String token, @RequestParam("reason") String reason, HttpServletResponse response) {
    	Long userNo = mypageService.getUserNo(token);
    	
    	// WithdrawDTO에 탈퇴 사유를 설정
        WithdrawDTO withdrawDTO = new WithdrawDTO();
        withdrawDTO.setUserNo(userNo);
        withdrawDTO.setReason(reason);

        mypageService.deleteUser(withdrawDTO);
        
        // 로그아웃 처리: JWT 쿠키 삭제
        Cookie jwtCookie = new Cookie("JWT", null); // 쿠키 값을 null로 설정
        jwtCookie.setMaxAge(0); // 쿠키 만료 시간 설정(즉시 만료)
        jwtCookie.setPath("/"); // 쿠키의 경로 설정
        response.addCookie(jwtCookie); // 응답에 쿠키 추가
        return "redirect:/"; // 사용자 목록 페이지로 리다이렉트
    }
}
    


