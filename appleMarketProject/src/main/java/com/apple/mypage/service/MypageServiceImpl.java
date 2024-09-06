package com.apple.mypage.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.apple.admin.repository.ProductReportRepository;
import com.apple.client.community.repository.CommunityPostRepository;
import com.apple.client.communityComment.repository.CommunityCommentRepository;
import com.apple.jwt.JwtUtil;
import com.apple.mypage.domain.Withdraw;
import com.apple.mypage.dto.MypageReviewDTO;
import com.apple.mypage.dto.WithdrawDTO;
import com.apple.mypage.repository.MypageRepository;
import com.apple.mypage.repository.WithdrawRepository;
import com.apple.order.repository.OrderRepository;
import com.apple.product.domain.OrderProductDTO;
import com.apple.product.domain.Product;
import com.apple.product.repository.ProductImagesRepository;
import com.apple.product.repository.ProductRepository;
import com.apple.user.domain.User;
import com.apple.user.repository.UserRepository;
import com.apple.usershop.domain.ItemReview;
import com.apple.usershop.domain.Usershop;
import com.apple.usershop.repository.UsershopRepository;
import com.apple.usershop.repository.UsershopReviewRepository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
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
    
    @Setter(onMethod_ = @Autowired)
    private WithdrawRepository withdrawRepository;
    
    @Setter(onMethod_ = @Autowired)
    private ProductReportRepository productReportRepository;
	
	@Setter(onMethod_ = @Autowired)
    private CommunityCommentRepository communityCommentRepository;
	
	@Setter(onMethod_ = @Autowired)
    private CommunityPostRepository communityPostRepository;
	
	@Setter(onMethod_ = @Autowired)
    private UsershopReviewRepository itemReviewRepository;
	
	@Setter(onMethod_ = @Autowired)
    private OrderRepository orderRepository;
	
	@Setter(onMethod_ = @Autowired)
    private ProductImagesRepository productImagesRepository;
	
	@Autowired
	private EntityManager entityManager;
    
    // 최근 3개월 동안 구매한 상품을 가져오는 메서드
    /*
    @Override
    public List<Product> getRecentBuyItemsByUserNo(Long userNo) {
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);
        return mypageRepository.findRecentBuyItemsByUserNo(userNo, threeMonthsAgo);
    }
    */
    
    @Override
    public List<OrderProductDTO> getBuyItemsByUserNo(Long userNo) {
        // 해당 userNo로 Order 목록을 가져옴
        // 해당 userNo로 apple_order 목록을 가져옴
        List<OrderProductDTO> orders = mypageRepository.findOrderProductInfoByUserNo(userNo);
        
        // 각 주문에서 productID를 사용하여 Product 목록을 반환
        return orders;
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
	
	@Override
	@Transactional
	public void deleteUser(WithdrawDTO withdrawDTO) {
		// 탈퇴 정보를 저장
        Withdraw withdraw = new Withdraw();
        withdraw.setUserNo(withdrawDTO.getUserNo());
        withdraw.setWithdrawDate(new Date());
        withdraw.setReason(withdrawDTO.getReason());

        withdrawRepository.save(withdraw);
        
        // 자식 엔티티들을 먼저 삭제
        Long userNo = withdrawDTO.getUserNo();

        try {
            // 1. mypage 데이터 삭제
            mypageRepository.deleteByUser_UserNo(userNo);
            entityManager.flush(); // 삭제 후 즉시 반영
            
            // 2. productReport 데이터 삭제
            productReportRepository.deleteByUser_UserNo(userNo);
            entityManager.flush(); // 삭제 후 즉시 반영
            
            // 3. itemReview 데이터 삭제
            itemReviewRepository.deleteByUser_UserNo(userNo);
            entityManager.flush(); // 삭제 후 즉시 반영
            
            // 4. communityComment 데이터 삭제
            communityCommentRepository.deleteByUserNo_UserNo(userNo);
            entityManager.flush(); // 삭제 후 즉시 반영
            
            // 5. communityPost 데이터 삭제
            communityPostRepository.deleteByUserNo_UserNo(userNo);
            entityManager.flush(); // 삭제 후 즉시 반영
            
            // 6. order 데이터 삭제
            orderRepository.deleteByUser_UserNo(userNo);
            entityManager.flush(); // 삭제 후 즉시 반영

            // 7. Product와 관련된 데이터 삭제
            List<Product> products = productRepository.findByUser_UserNo(userNo);
            entityManager.flush(); // 삭제 후 즉시 반영

            // ProductImages 삭제
            for (Product product : products) {
                productImagesRepository.deleteByProduct(product);
                entityManager.flush(); // 삭제 후 즉시 반영
            }

            // Product 삭제
            productRepository.deleteByUser_UserNo(userNo);
            entityManager.flush(); // 삭제 후 즉시 반영

            // 8. Usershop 데이터 삭제
            usershopRepository.deleteByUser_UserNo(userNo);
            entityManager.flush(); // 삭제 후 즉시 반영

            // 9. 회원 데이터 삭제
            userRepository.deleteByUserNo(userNo);
            entityManager.flush(); // 삭제 후 즉시 반영

        } catch (Exception e) {
            // 예외 발생 시 처리
            throw new RuntimeException("삭제 중 오류 발생: " + e.getMessage());
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

	@Override
	public Long getUserNoByProductID(Long productID) {
		return productRepository.findUserNoByProductID(productID);
	}
	
	@Override
	// userNo로 productId를 가져오는 서비스 메서드
    public List<Long> getProductIdsByUserNo(Long userNo) {
        return itemReviewRepository.findProductIdsByUserNo(userNo);
    }
	
	// userNo와 productID로 review 조회
    @Override
	public Optional<ItemReview> getReviewByUserNoAndProductId(Long userNo, Long productID) {
        return itemReviewRepository.findByUserNoAndProductId(userNo, productID);
    }
    
    @Override
    public void updateReviewById(Long reviewNo, MypageReviewDTO reviewDto) {
        // 리뷰 ID로 기존 리뷰를 조회
        Optional<ItemReview> reviewOptional = usershopReviewRepository.findById(reviewNo);
        if (reviewOptional.isPresent()) {
            ItemReview existingReview = reviewOptional.get();

            // 기존 리뷰의 내용을 수정
            existingReview.setReviewContent(reviewDto.getReviewContent());
            existingReview.setStarRating(reviewDto.getStarRating());
            existingReview.setSelectReview1(reviewDto.getSelectReview1());
            existingReview.setSelectReview2(reviewDto.getSelectReview2());
            existingReview.setSelectReview3(reviewDto.getSelectReview3());
            existingReview.setSelectReview4(reviewDto.getSelectReview4());
            existingReview.setSelectReview5(reviewDto.getSelectReview5());

            // 수정된 리뷰 저장
            usershopReviewRepository.save(existingReview);
        } else {
            throw new IllegalArgumentException("Invalid review ID: " + reviewNo);
        }
    }
    
    @Override
    @Transactional
    public void deleteReviewById(Long reviewNo) {
        // 리뷰 ID로 해당 리뷰 삭제
        usershopReviewRepository.deleteById(reviewNo);
    }

}
