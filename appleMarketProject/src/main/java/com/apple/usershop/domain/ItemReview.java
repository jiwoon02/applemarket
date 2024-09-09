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
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ToString
@Entity
@Table(name="review")
@SequenceGenerator(name="review_generator", sequenceName="apple_review_seq", initialValue=1, allocationSize=1)
public class ItemReview {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="review_generator")
	private long reviewNo;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopId", referencedColumnName = "shopId")
    private Usershop usershop;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", referencedColumnName = "user_no")
    private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private Product product;
	
	@ColumnDefault(value = "0")
	private long starRating;
	
	@Lob
	private String reviewContent;
	
	@ColumnDefault(value = "sysdate")
	private LocalDateTime reviewRegDate;
	
	@ColumnDefault(value = "0")
	private long selectReview1;
	
	@ColumnDefault(value = "0")
	private long selectReview2;
	
	@ColumnDefault(value = "0")
	private long selectReview3;
	
	@ColumnDefault(value = "0")
	private long selectReview4;
	
	@ColumnDefault(value = "0")
	private long selectReview5;
}
