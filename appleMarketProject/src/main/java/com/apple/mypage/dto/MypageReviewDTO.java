package com.apple.mypage.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MypageReviewDTO {
	private Long userNo;
    private String shopId;
    private long starRating;
    private String reviewContent;
    private long productID;
    private long selectReview1;
    private long selectReview2;
    private long selectReview3;
    private long selectReview4;
    private long selectReview5;
}
