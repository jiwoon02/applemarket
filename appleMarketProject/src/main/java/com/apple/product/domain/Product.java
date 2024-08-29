package com.apple.product.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
//import java.util.List;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import org.springframework.web.multipart.MultipartFile;

import com.apple.category.domain.Category;
import com.apple.user.domain.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
//import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
//import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
@SequenceGenerator(name="product_generator", sequenceName = "apple_product_seq", initialValue = 1, allocationSize = 1)
public class Product {

	@Id
	@Column(name = "product_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_generator")
	private Long productID; // 대표키
	
	@ManyToOne
	@JoinColumn(name = "user_no") // 이게 맞는지 확인해야할듯? userID 인지 user_id 인지
	private User user;

	@ManyToOne
	@JoinColumn(name = "category_id") // 이게 맞는지 확인해야할듯?
	private Category category;

	@Column(name = "product_price", nullable = false)
	private int productPrice;

	@Column(name = "post_price")
	@ColumnDefault(value="0")
	private int postPrice;

	@Column(name = "product_name", length = 50, nullable = false)
	private String productName;

	@Column(name = "product_description", length = 1000, nullable = false)
	private String productDescription;

	@CreationTimestamp
	@Column(name = "product_reg_date", nullable = false)
	@ColumnDefault(value="sysdate")
	private LocalDateTime productRegDate;		//등록일
	
	@UpdateTimestamp
	@Column(name = "product_update_date", nullable = false)
	@ColumnDefault(value="sysdate")
	private LocalDateTime productUpdateDate;  	//수정일

	@Column(name = "product_visit_count", nullable = false)
	@ColumnDefault(value = "0")
	private int productVisitCount;		//상품조회수

	// 상품하나에 사진5개까지허용(썸네일포함) productImages에는 4개까지 허용
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductImages> productImages = new ArrayList<>();

	@Transient
	private List<MultipartFile> files; //여러개 파일 업로드
	
    @Transient
    private List<String> deletedImageFilenames; // 삭제할 파일 이름 리스트
	
}
