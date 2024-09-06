package com.apple.mypage.service;

import java.util.List;
import java.util.Optional;

import com.apple.mypage.dto.MypageReviewDTO;
import com.apple.mypage.dto.WithdrawDTO;
import com.apple.product.domain.OrderProductDTO;
import com.apple.product.domain.Product;
import com.apple.user.domain.User;
import com.apple.usershop.domain.ItemReview;

public interface MypageService {
	
	public List<OrderProductDTO> getBuyItemsByUserNo(Long userNo);
	
    public List<Product> getAllItemsByUserNo(Long userNo);
    
    public void deleteBuyItem(Long userNo, Long productID);
    
    public void deleteSellItem(Long userNo, Long productID);
    
    public Product getItemByProductId(Long productID);
    
    public String getUserNicknameByShopId(Long shopId);
    
    public void addReview(MypageReviewDTO reviewDtom);
    
    public boolean checkPassword(Long userNo, String inputPassword);
    
    public void updateUserInfo(Long userNo, User updatedUser);

	Long getUserNo(String token);
	
	public void deleteUser(WithdrawDTO withdrawDTO);
	
	public Long getUserNoByProductID(Long productID);
	
	public List<Long> getProductIdsByUserNo(Long userNo);
	
    public Optional<ItemReview> getReviewByUserNoAndProductId(Long userNo, Long productID);
    
    public void updateReviewById(Long reviewNo, MypageReviewDTO reviewDto);
    
    public void deleteReviewById(Long reviewNo);
}
