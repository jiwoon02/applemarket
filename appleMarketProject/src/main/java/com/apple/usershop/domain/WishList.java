package com.apple.usershop.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import com.apple.product.domain.Product;
import com.apple.user.domain.User;

import groovy.transform.ToString;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ToString
@Entity
@Table(name="wishlist")
@SequenceGenerator(name="wishlist_generator", sequenceName="apple_wish_list_seq", initialValue=1, allocationSize=1)
public class WishList {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="wishlist_generator")
	private long wishListNo;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", referencedColumnName = "user_no")
    private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private Product product;
	
	@ColumnDefault(value = "sysdate")
	private LocalDateTime wishListRegDate;
}
