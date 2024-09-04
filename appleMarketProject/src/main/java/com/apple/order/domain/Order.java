package com.apple.order.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.web.multipart.MultipartFile;

import com.apple.product.domain.Product;
import com.apple.product.domain.ProductImages;
import com.apple.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "apple_order")
public class Order {

	/*기본 키 매핑*/
	@Id
	@Column(name="order_id", length = 100)
	private String orderID;					//주문 ID
	
	
	/*각 객체 필드 -> 컬럼 테이블에 매핑*/
//	@CreationTimestamp
	@ColumnDefault(value="sysdate")
	@Column(name="order_reg_date", nullable = false)
	private LocalDateTime orderRegDate;		//주문일
	
	@Column(name="payment_method", length = 50, nullable = false)
	private String paymentMethod;			//결제수단
	
	@Column(name="post_address", nullable = false)
	private String postAddress;				//배송지
	
	@Column(name="post_number", length = 50)
	private String postNumber;				//운송장 번호
	
	@Column(name="request_text", length = 50)
	private String requestText;				//요청사항
	
	
	/*외래 키 생성*/
	@ManyToOne								//Order(다)-User(일) 를 다대일 관계로 설정
	@JoinColumn(name = "user_no")			//외래키 생성, User 엔티티의 기본키(userNo)와 매핑
	private User user;						//이 주문에 해당하는 회원(구매자)
	
	/*
	 * @OneToOne //Product - Order 를 일대일 관계로 설정(이하 컬럼들 모두 동일)
	 * 
	 * @JoinColumns({
	 * 
	 * @JoinColumn(name="product_id", referencedColumnName="product_id"), //상품ID
	 * 
	 * @JoinColumn(name="product_price", referencedColumnName="product_price"),
	 * //상품가격
	 * 
	 * @JoinColumn(name="product_name", referencedColumnName="product_name") //상품명
	 * }) private Product product;
	 */

	
	@OneToOne								//Product - Order 를 일대일 관계로 설정(이하 컬럼들 모두 동일)
	@JoinColumn(name="product_id", referencedColumnName="product_id")			//상품ID
	private Product product;				

	@OneToOne
	@JoinColumn(name="product_image_id", referencedColumnName="product_image_id")
	private ProductImages productImages;	//상품이미지ID
	
	//@Transient: 필드를 매핑하지 않을 때 사용
	@Transient
	private MultipartFile file;	//이미지 파일을 위한 필드(컬럼으로 사용 x)
	
	
	/*@PrePersist: Entity가 insert되기 전에 호출됨*/
	@PrePersist
    public void newOrderId() {
		
		//현재 시간 설정
		this.orderRegDate = LocalDateTime.now();
		
        // user_no, orderRegDate를 기반으로 orderID 생성 
        if (this.user != null && this.user.getUserNo() != null && this.orderRegDate != null) {
            this.orderID = this.user.getUserNo() + "O" + this.orderRegDate.format(DateTimeFormatter.ofPattern("YYYYMMDDhhmmssSSSSSS"));
        } 
        else if(this.user == null || this.user.getUserNo() == null){
            throw new IllegalStateException("OrderID 생성중 예외발생 - User 정보가 없습니다.");
        }
        else {
        	throw new IllegalStateException("OrderID 생성중 예외발생 - orderRegDate 정보가 없습니다.");
        }
        
    }
	
}
