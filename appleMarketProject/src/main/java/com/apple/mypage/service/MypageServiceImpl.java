package com.apple.mypage.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;

import com.apple.jwt.JwtUtil;
import com.apple.mypage.domain.Test_order;
import com.apple.mypage.dto.MypageReviewDTO;
import com.apple.mypage.repository.MypageRepository;
import com.apple.order.domain.Order;
import com.apple.product.domain.Product;
import com.apple.product.repository.ProductRepository;
import com.apple.user.domain.User;
import com.apple.user.repository.UserRepository;
import com.apple.usershop.domain.ItemReview;
import com.apple.usershop.domain.Usershop;
import com.apple.usershop.repository.UsershopRepository;
import com.apple.usershop.repository.UsershopReviewRepository;

import lombok.Setter;

@Service
public class MypageServiceImpl implements MypageService {
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
    @Setter(onMethod_ = @Autowired)
    private MypageRepository mypageRepository;
    
    @Setter(onMethod_ = @Autowired)
    private UsershopRepository usershopRepository;
    
    @Setter(onMethod_ = @Autowired)
    private UserRepository userRepository;
    
    @Setter(onMethod_ = @Autowired)
    private UsershopReviewRepository usershopReviewRepository;
    
    @Setter(onMethod_ = @Autowired)
    private ProductRepository productRepository;
    
    // 최근 3개월 동안 구매한 상품을 가져오는 메서드
    @Override
    public List<Product> getRecentBuyItemsByUserNo(Long userNo) {
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);
        return mypageRepository.findRecentBuyItemsByUserNo(userNo, threeMonthsAgo);
    }
    
    @Override
    public List<Product> getBuyItemsByUserNo(Long userNo) {
        // 해당 userNo로 Test_order 목록을 가져옴
        List<Order> orders = mypageRepository.findByUserUserNo(userNo);
        
        // 각 주문에서 productID를 사용하여 Product 목록을 반환
        return orders.stream()
                     .map(Order::getProduct)
                     .collect(Collectors.toList());
    }
    
    @Override
    public List<Product> getItemsExcludingOrders(Long userNo) {
        return mypageRepository.findItemsExcludingOrders(userNo);
    }
    
    @Override
    public List<Product> getSoldItems(Long userNo) {
        return mypageRepository.findSoldItems(userNo);
    }
    
    // userNo로 전체 상품 목록을 가져오는 메서드
    @Override
    public List<Product> getAllItemsByUserNo(Long userNo) {
        return mypageRepository.findAllItemsByUserNo(userNo);
    }
    
    @Override
    public void deleteBuyItem(Long userNo, Long productID) {
        mypageRepository.deleteOrderByUserNoAndProductId(userNo, productID);
    }
    
    @Override
    public List<String> getItemStatusByUserNo(Long userNo) {
        return mypageRepository.findItemStatusByUserNo(userNo);
    }

    @Override
    public void deleteSellItem(Long userNo, Long productID) {
    	try {
            // 첫 번째 삭제 작업을 수행
            mypageRepository.deleteOrderByUserNoAndProductId(userNo, productID);
            
            // 첫 번째 작업이 성공적으로 완료된 후에 두 번째 삭제 작업을 수행
            mypageRepository.deleteItemByUserNoAndProductId(userNo, productID);
        } catch (Exception e) {
            // 첫 번째 작업에서 예외가 발생하면 로그를 남기고 예외를 던집니다.
            // 필요에 따라 예외 처리 로직을 추가할 수 있습니다.
            System.err.println("첫 번째 삭제 작업 실패: " + e.getMessage());
            throw e;  // 예외를 다시 던져 호출자에게 알림
        }
    }
    
    @Override
    public Product getItemByProductId(Long productID) {
        return mypageRepository.findItemByProductId(productID)
            .orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));
    }
    
    // shopId로 userNickname을 가져오는 메서드 추가
    @Override
    public String getUserNicknameByShopId(Long shopId) {
        Usershop usershop = usershopRepository.findByShopId(shopId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid shop ID"));

        Optional<User> optionalUser = userRepository.findByUserNo(usershop.getUser().getUserNo());
        User user = optionalUser.get();

        return user.getUserNickname();
    }
    
    @Override
    public void addReview(MypageReviewDTO reviewDto) {
        User user = userRepository.findById(reviewDto.getUserNo()).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        Usershop usershop = usershopRepository.findById(reviewDto.getShopId()).orElseThrow(() -> new IllegalArgumentException("Invalid shop ID"));
        Product product = productRepository.findById(reviewDto.getProductID()).orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));
        
        ItemReview review = new ItemReview();
        review.setUser(user);
        review.setUsershop(usershop);
        review.setStarRating(reviewDto.getStarRating());
        review.setReviewContent(reviewDto.getReviewContent());
        review.setProduct(product);

        review.setSelectReview1(reviewDto.getSelectReview1());
        review.setSelectReview2(reviewDto.getSelectReview2());
        review.setSelectReview3(reviewDto.getSelectReview3());
        review.setSelectReview4(reviewDto.getSelectReview4());
        review.setSelectReview5(reviewDto.getSelectReview5());
        
        usershopReviewRepository.save(review);
    }

	@Override
	public boolean checkPassword(Long userNo, String inputPassword) {
		Optional<User> optionalUser = userRepository.findByUserNo(userNo);
		User user = optionalUser.get();
        if (user != null) {
        	// passwordEncoder를 사용하여 암호화된 비밀번호와 입력된 비밀번호 비교
            return passwordEncoder.matches(inputPassword, user.getUserPwd());
        }
        return false;
	}

	@Override
    public void updateUserInfo(Long userNo, User updatedUser) {
		Optional<User> optionalUser = userRepository.findByUserNo(userNo);
		User existingUser = optionalUser.get();
        if (existingUser != null) {
        	String encryptedPassword = passwordEncoder.encode(updatedUser.getUserPwd());
        	existingUser.setUserPwd(encryptedPassword);  // 비밀번호 수정
            existingUser.setUserName(updatedUser.getUserName());  // 이름 수정
            existingUser.setUserNickname(updatedUser.getUserNickname());  // 닉네임 수정
            existingUser.setUserPhone(updatedUser.getUserPhone());  // 전화번호 수정
            existingUser.setUserEmail(updatedUser.getUserEmail());  // 이메일 수정
            existingUser.setUserBirth(updatedUser.getUserBirth());  // 생년월일 수정

            userRepository.save(existingUser); // 변경된 내용을 저장
        }
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
