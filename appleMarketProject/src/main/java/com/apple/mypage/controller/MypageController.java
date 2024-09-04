package com.apple.mypage.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.apple.mypage.service.MypageService;
import com.apple.product.domain.Product;
import com.apple.user.domain.User;
import com.apple.user.repository.UserRepository;
import com.apple.usershop.domain.ItemReview;
import com.apple.usershop.domain.Usershop;
import com.apple.usershop.repository.UsershopRepository;
import com.apple.usershop.repository.UsershopReviewRepository;
import com.apple.usershop.service.UsershopService;

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
    private JwtUtil jwtUtil;
    
    //마이페이지
//    @GetMapping("{userNo}")
//    public String getRecentBuyItemsByUserNo(@PathVariable Long userNo, Model model) {
//        List<Product> items = mypageService.getRecentBuyItemsByUserNo(userNo);
//        model.addAttribute("items", items);
//        return "mypage/mypage"; // 반환할 뷰의 이름
//    }
    
    @GetMapping("")
    public String getRecentBuyItemsByUserNo(@CookieValue(value="JWT", required=false) String token, Model model) {
    	Long userNo = mypageService.getUserNo(token);
    	
    	List<Product> items = mypageService.getRecentBuyItemsByUserNo(userNo);
    	model.addAttribute("items", items);
	 
    	return "mypage/mypage"; // 반환할 뷰의 이름
    }
    
    @GetMapping("/mypage")
    public String getRecentBuyItemsByUserNo(@PathVariable Long userNo, Model model) {
        List<Product> items = mypageService.getRecentBuyItemsByUserNo(userNo);
        model.addAttribute("items", items);
        return "mypage/mypage"; // 반환할 뷰의 이름
    }
    
    //판매상품
    @GetMapping("/sell")
    public String getItemsExcludingOrders(@CookieValue(value="JWT", required=false) String token, Model model) {
    	Long userNo = mypageService.getUserNo(token);
    	model.addAttribute("userNo", userNo);
    	
        List<Product> items = mypageService.getItemsExcludingOrders(userNo);
        model.addAttribute("items", items);
        return "mypage/mypageSellItem"; // 반환할 뷰의 이름
    }
    
	@GetMapping("/buy")
	public String getBuyItemsByUserNo(@CookieValue(value="JWT", required=false) String token, Model model) {
		Long userNo = mypageService.getUserNo(token);
		model.addAttribute("userNo", userNo);
		
	    List<Product> items = mypageService.getBuyItemsByUserNo(userNo);
	    model.addAttribute("items", items);

	    return "mypage/mypageBuyItem"; // 반환할 뷰의 이름
	}
    
    @GetMapping("sold")
    public String getSoldItems(@CookieValue(value="JWT", required=false) String token, Model model) {
    	Long userNo = mypageService.getUserNo(token);
    	model.addAttribute("userNo", userNo);
    	
        List<Product> items = mypageService.getSoldItems(userNo);
        model.addAttribute("items", items);
        
        return "mypage/mypageSellItem"; // 반환할 뷰의 이름
    }
    
    @GetMapping("sellAll")
    public String getAllItemsByUserNo(@CookieValue(value="JWT", required=false) String token, Model model) {
    	Long userNo = mypageService.getUserNo(token);
    	model.addAttribute("userNo", userNo);
    	
        List<Product> items = mypageService.getAllItemsByUserNo(userNo);
        List<String> itemStatuses = mypageService.getItemStatusByUserNo(userNo); // 각 상품의 상태 가져오기
        
        model.addAttribute("items", items);
        model.addAttribute("itemStatuses", itemStatuses); // 상태를 모델에 추가
        return "mypage/mypageSellItem"; // 반환할 뷰의 이름
    }
    
    // 특정 상품을 삭제하는 메서드
    @PostMapping("/delete/buy")
    public String deleteBuyItem(@CookieValue(value="JWT", required=false) String token, @RequestParam("productIds[]") List<Long> productID, Model model) {
    	Long userNo = mypageService.getUserNo(token);
    	for (Long product : productID) {
            mypageService.deleteBuyItem(userNo, product);
        }

        return "redirect:/mypage/buy" + userNo; // 반환할 뷰의 이름
    }
    
    // 특정 상품을 삭제하는 메서드
    @PostMapping("/delete/sell{userNo}")
    public String deleteSellItem(@PathVariable Long userNo, @RequestParam("productIds[]") List<Long> productID, Model model) {
        for (Long product : productID) {
            mypageService.deleteSellItem(userNo, product);
        }

        return "redirect:/mypage/sellAll" + userNo; // 삭제 후 전체 목록으로 리다이렉트
    }
    
    // 리뷰 작성 페이지를 보여주는 메서드 추가
    @GetMapping("/addReview/{productName}/{shopId}/{productID}/{userNo}")
    public String showAddReviewForm(@PathVariable String productName, 
                                    @PathVariable Long shopId, 
                                    @PathVariable Long productID, 
                                    @PathVariable Long userNo,
                                    Model model) {
    	// shopId를 사용하여 userNickname을 가져옴
        String userNickname = mypageService.getUserNicknameByShopId(shopId);
        
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
        
        // 필요한 데이터 모델에 추가
        model.addAttribute("userNickname", userNickname);
        model.addAttribute("productName", productName);
        model.addAttribute("shopId", shopId);
        model.addAttribute("userNo", userNo);
        model.addAttribute("productID", productID);

        return "mypage/mypageReview"; // 리뷰 작성 페이지로 이동
    }
    
    // 리뷰를 추가하는 메서드
    @PostMapping("/addReview")
    public String addReview(@ModelAttribute MypageReviewDTO reviewDto) {
    	
        mypageService.addReview(reviewDto);
        return "mypage/mypage"; // 수정 필요 !!!!!!!!
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
    
}

