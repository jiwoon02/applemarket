package com.apple.product.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderProductDTO {
	private String orderId;              // 주문 ID
    private LocalDateTime orderRegDate;  // 주문일시
    private Long productId;              // 상품 ID
    private String productName;          // 상품명
    private int productPrice;            // 상품 가격
}
