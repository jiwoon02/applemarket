package com.apple.mypage.service;

import java.util.List;

import com.apple.mypage.dto.MypageReviewDTO;
import com.apple.product.domain.Product;
import com.apple.user.domain.User;

public interface MypageService {
	
	public List<Product> getRecentBuyItemsByUserNo(Long userNo);
	
	public List<Product> getBuyItemsByUserNo(Long userNo);
	
	public List<Product> getItemsExcludingOrders(Long userNo);
	
    public List<Product> getSoldItems(Long userNo);
	
    public List<Product> getAllItemsByUserNo(Long userNo);
    
    public void deleteBuyItem(Long userNo, Long productID);
    
    public List<String> getItemStatusByUserNo(Long userNo);
    
    public void deleteSellItem(Long userNo, Long productID);
    
    public Product getItemByProductId(Long productID);
    
    public String getUserNicknameByShopId(Long shopId);
    
    public void addReview(MypageReviewDTO reviewDtom);
    
    public boolean checkPassword(Long userNo, String inputPassword);
    
    public void updateUserInfo(Long userNo, User updatedUser);

	Long getUserNo(String token);
}
