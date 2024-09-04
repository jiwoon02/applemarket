package com.apple.usershop.service;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apple.product.domain.Product;
import com.apple.product.repository.ProductRepository;
import com.apple.user.domain.User;
import com.apple.user.repository.UserRepository;
import com.apple.usershop.domain.ItemReview;
import com.apple.usershop.domain.Usershop;
import com.apple.usershop.repository.UsershopRepository;
import com.apple.usershop.repository.UsershopReviewRepository;

import jakarta.transaction.Transactional;
import lombok.Setter;

@Service
public class UsershopServiceImpl implements UsershopService {

    @Setter(onMethod_ = @Autowired)
    private UsershopRepository usershopRepository;
    
    @Setter(onMethod_ = @Autowired)
    private ProductRepository productRepository;
    
    @Setter(onMethod_ = @Autowired)
    private UsershopReviewRepository usershopReviewRepository;
    
    @Setter(onMethod_ = @Autowired)
    private UserRepository userRepository;

	@Override
	public List<Usershop> usershop(Usershop usershop) {
		
		if (usershop.getUser() != null && usershop.getUser().getUserNo() != null) {
            // 특정 userNo를 기준으로 데이터 조회
            return usershopRepository.findByUser_UserNo(usershop.getUser().getUserNo());
        } else {
            // 모든 데이터를 조회
            return usershopRepository.findAll();
        }
	}

	@Override
	public List<Product> usershopItemListByUserNo(Long userNo) {
        // 특정 userNo를 기준으로 데이터 조회
        return productRepository.findByUser_UserNo(userNo);
	}

	@Override
	public Long countProductsByUserNo(Long userNo) {
		return productRepository.countByUser_UserNo(userNo);
	}
	
	@Override
    public List<ItemReview> usershopReviewListByShopId(Long shopId) {
        return usershopReviewRepository.findByUsershop_ShopId(shopId);
    }

	@Override
	public long calculateAverageStarRating(List<ItemReview> reviews) {
        OptionalDouble average = reviews.stream()
            .mapToDouble(ItemReview::getStarRating)
            .average();

        // 평균이 존재하는 경우 소수점 이하 버림 후 정수로 반환, 없으면 0 반환
        return average.isPresent() ? (long) Math.floor(average.getAsDouble()) : 0;
	}

	@Override
	public void usershopInsert(Usershop usershop) {
		usershopRepository.save(usershop);
	}
	
	@Override
    public Usershop findByUserNo(Long userNo) {
        return usershopRepository.findByUser_UserNo(userNo)
            .stream()
            .findFirst()  // 여러 개일 경우 첫 번째 것만 반환
            .orElse(null);
    }

	@Override
    @Transactional
    public void updateShopIntroduce(Long shopId, String shopIntroduce) {
        Optional<Usershop> optionalUsershop = usershopRepository.findById(shopId);
        if (optionalUsershop.isPresent()) {
            Usershop usershop = optionalUsershop.get();
            usershop.setShopIntroduce(shopIntroduce);
            usershopRepository.save(usershop);  // 변경 사항 저장
        } else {
            throw new RuntimeException("Usershop not found for shopId: " + shopId);
        }
	}
	
	@Override
    @Transactional
    public void updateUserNickname(Long userNo, String newNickname) {
		Optional<User> optionalUser = userRepository.findByUserNo(userNo);
        if (optionalUser.isPresent()) {
        	User user = optionalUser.get();
            user.setUserNickname(newNickname);
            userRepository.save(user);  // 변경 사항 저장
        } else {
            throw new RuntimeException("User not found for userNo: " + userNo);
        }
    }
	
	@Override
	public long calculateSatisfactionPercentage(List<ItemReview> reviews) {
	    if (reviews == null || reviews.isEmpty()) {
	        return 0;
	    }

	    long satisfactionCount = reviews.stream()
	        .filter(review -> review.getStarRating() >= 4)  // 예를 들어, 4점 이상을 만족 후기로 간주
	        .count();

	    double percentage = ((double) satisfactionCount / reviews.size()) * 100;

	    return Math.round(percentage);  // 소수점 첫째 자리에서 반올림하여 정수를 반환
	}
	
	@Override
	public Usershop findByShopId(Long shopId) {
	    return usershopRepository.findById(shopId).orElse(null);
	}

	@Override
	@Transactional
	public void updateUsershop(Usershop usershop) {
	    usershopRepository.save(usershop);
	}

	@Override
	public void shopVisitCount(Long shopId) {
		Optional<Usershop> optionalUsershop = usershopRepository.findById(shopId);
        if (optionalUsershop.isPresent()) {
            Usershop usershop = optionalUsershop.get();
            usershop.setShopVisitCount(usershop.getShopVisitCount() + 1);
            usershopRepository.save(usershop);  // 변경 사항 저장
        } else {
            throw new RuntimeException("Usershop not found for shopId: " + shopId);
        }
	}
	
	// shopId를 기준으로 각 selectReview 항목의 합을 구하는 메서드
    @Override
    public long sumSelectReview1ByShopId(Long shopId) {
    	Long result = usershopReviewRepository.sumSelectReview1ByShopId(shopId);
        return result != null ? result : 0L;
    }
    
    @Override
    public long sumSelectReview2ByShopId(Long shopId) {
    	Long result = usershopReviewRepository.sumSelectReview2ByShopId(shopId);
        return result != null ? result : 0L;
    }
    
    @Override
    public long sumSelectReview3ByShopId(Long shopId) {
    	Long result = usershopReviewRepository.sumSelectReview3ByShopId(shopId);
        return result != null ? result : 0L;
    }
    
    @Override
    public long sumSelectReview4ByShopId(Long shopId) {
    	Long result = usershopReviewRepository.sumSelectReview4ByShopId(shopId);
        return result != null ? result : 0L;
    }
    
    @Override
    public long sumSelectReview5ByShopId(Long shopId) {
    	Long result = usershopReviewRepository.sumSelectReview5ByShopId(shopId);
        return result != null ? result : 0L;
    }
    
    @Override
	public Usershop createUsershop(User user) {
        Usershop usershop = new Usershop();
        usershop.setUser(user);
        
        // 기본값 설정
        usershop.setShopVisitCount(0L);
        usershop.setShopIntroduce("소개글을 입력해 주세요.");
        
        // Usershop 객체 저장
        return usershopRepository.save(usershop);
    }
}

