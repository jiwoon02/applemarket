package com.apple.usershop.service;

import java.util.List;

import com.apple.product.domain.Product;
import com.apple.user.domain.User;
import com.apple.usershop.domain.ItemReview;
import com.apple.usershop.domain.Usershop;
import com.apple.usershop.domain.WishList;

public interface UsershopService {
	public List<Usershop> usershop(Usershop usershop);
	
	public List<Product> usershopItemListByUserNo(Long userNo);
	// userNo를 기준으로 productId의 개수를 가져오는 메서드
    public Long countProductsByUserNo(Long userNo);
    
    // shopId를 기준으로 ItemReview 리스트를 가져오는 메서드
    public List<ItemReview> usershopReviewListByShopId(Long shopId);
    
    // 별점 계산
    public long calculateAverageStarRating(List<ItemReview> reviews);
    
    // 상점 입력
    public void usershopInsert(Usershop usershop);
    
    public Usershop findByUserNo(Long userNo);
    
    public void updateShopIntroduce(Long shopId, String shopIntroduce);  // shopIntroduce 업데이트
    
    public long calculateSatisfactionPercentage(List<ItemReview> reviews);
    
    public Usershop findByShopId(Long shopId);
    
    public void updateUsershop(Usershop usershop);
    
    public void shopVisitCount(Long shopId);
    
    public long sumSelectReview1ByShopId(Long shopId);
    
    public long sumSelectReview2ByShopId(Long shopId);
    
    public long sumSelectReview3ByShopId(Long shopId);
    
    public long sumSelectReview4ByShopId(Long shopId);
    
    public long sumSelectReview5ByShopId(Long shopId);
    
    public Usershop createUsershop(User user);
    
    public List<WishList> getWishListByUserNo(Long userNo);
}
