package com.apple.mypage.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import com.apple.product.domain.Product;
import com.apple.user.domain.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "test_apple_order")
public class Test_order {

    @Id
    private String orderId;

    // 구매자 (여러 개의 주문이 하나의 사용자에게 연결될 수 있음)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", referencedColumnName = "user_no")
    private User user;

    // 주문한 상품 (하나의 주문은 하나의 상품만 참조)
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private Product item;

    @ColumnDefault(value = "sysdate")
    private LocalDateTime orderRegDate;

    @Column
    private String paymentMethod;

    @Column
    private String postAddress;

    @Column
    private String requestText;

    @Column
    private String postNumber;
}
