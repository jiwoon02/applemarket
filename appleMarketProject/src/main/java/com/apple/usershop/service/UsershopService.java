package com.apple.usershop.service;

import java.util.List;

import com.apple.product.domain.Product;
import com.apple.usershop.domain.ItemReview;
import com.apple.usershop.domain.Usershop;

public interface UsershopService {
	public List<Usershop> usershop(Usershop usershop);
	
	public List<Product> usershopItemListByUserNo(Long userNo);
	// userNo를 기준으로 productId의 개수를 가져오는 메서드
    public Long countProductsByUserNo(Long userNo);
    
    // shopId를 기준으로 ItemReview 리스트를 가져오는 메서드
    public List<ItemReview> usershopReviewListByShopId(String shopId);
    
    // 별점 계산
    public long calculateAverageStarRating(List<ItemReview> reviews);
    
    // 상점 입력
    public void usershopInsert(Usershop usershop);
    
    public Usershop findByUserNo(Long userNo);
    
    public void updateShopIntroduce(String shopId, String shopIntroduce);  // shopIntroduce 업데이트
    
    public void updateUserNickname(Long userNo, String newNickname);  // userNo를 기준으로 userNickname 수정
    
    public long calculateSatisfactionPercentage(List<ItemReview> reviews);
    
    public Usershop findByShopId(String shopId);
    
    public void updateUsershop(Usershop usershop);
    
    public void shopVisitCount(String shopId);
    
    public long sumSelectReview1ByShopId(String shopId);
    
    public long sumSelectReview2ByShopId(String shopId);
    
    public long sumSelectReview3ByShopId(String shopId);
    
    public long sumSelectReview4ByShopId(String shopId);
    
    public long sumSelectReview5ByShopId(String shopId);
}
