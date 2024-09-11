package com.apple.user.domain;

import java.util.Date;
import java.util.List;

import com.apple.admin.domain.ProductReport;
import com.apple.client.community.domain.CommunityPost;
import com.apple.client.communityComment.domain.CommunityComment;
import com.apple.location.domain.Location;
import com.apple.product.domain.Product;
import com.apple.usershop.domain.ItemReview;
import com.apple.usershop.domain.Usershop;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="apple_user")
@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
/* JPA를 사용해서 테이블과 매핑할 클래스 - 이 어노테이션이 명시된 클래스는 JPA가 관리하는 것으로 엔티티라 부른다.*/

/* 엔티티와 매핑할 테이블을 지정 - 생략하면 매핑할 엔티티 이름을 테이블명으로 사용. 
    name 속성을 이용해서 테이블명 지정. */
public class User{
	/* 기본키(엔티티를 식별할 때 사용할 필드를 지정할 때 사용)를 매핑 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_generator")
	@SequenceGenerator(name = "user_seq_generator", sequenceName = "apple_user_seq", allocationSize = 1)
	@Column(name = "user_no", nullable = false, unique = true)
    private Long userNo; // 시퀀스로 생성되는 회원 일련번호 기본키X
	
	@Column(name = "user_id", nullable = false, unique = true, length = 12)
    private String userID; // 회원 아이디 - VARCHAR2(12)
	
    @Column(name = "user_pwd", nullable = false, length = 255)
    private String userPwd; // 비밀번호 - VARCHAR2(25)

    @Column(name = "user_name", nullable = false, length = 20)
    private String userName; // 이름 - VARCHAR2(20)
    
    @Column(name = "user_phone", nullable = false, unique = true, length = 14)
    private String userPhone; // 전화번호 - VARCHAR2(14)
    
    @Column(name = "user_email", nullable = false, unique = true, length = 100)
    private String userEmail; // 이메일 - VARCHAR2(100)
    
    @Column(name = "user_birth", nullable = false, length = 12)
    private String userBirth; // 생년월일 - VARCHAR2(12)

    @Column(name = "user_nickname", nullable = false, unique = true, length = 30)
    private String userNickname; // 닉네임 - VARCHAR2(30)
    
    @Column(name = "user_zonecode", nullable = false, length = 10)
    private String userZonecode; // 우편번호 - VARCHAR2(10)
    
    @Column(name = "user_address", nullable = false, length = 100)
    private String userAddress; // 주소 - VARCHAR2(100)
    
    @Column(name = "user_address_detail", nullable = false, length = 50)
    private String userAddressDetail; // 상세주소 - VARCHAR2(50)

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "user_reg_date", nullable = false)
    private Date userRegDate; // 가입일

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "user_edit_date", nullable = false)
    private Date userEditDate; // 수정일

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = true)
    private Location location; // Location 엔티티와의 관계, 외래키
    
    @Column(name = "user_role", nullable = false, length = 10)
    private String userRole; // 권한 - VARCHAR2(10)
    
    @PrePersist
    protected void onCreate() {
        this.userRegDate = new Date();  // 가입일을 현재 시간으로 설정
        this.userEditDate = new Date(); // 수정일도 현재 시간으로 설정
        this.userRole = "USER";
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.userEditDate = new Date(); // 수정일을 현재 시간으로 설정
    }
    
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Usershop usershop;
	
	// Test_user 엔티티는 여러 개의 Test_item 엔티티를 가질 수 있음
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
	private ProductReport productReport;
	
    @OneToMany(mappedBy = "userNo", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<CommunityComment> communityComment;
	
	@OneToMany(mappedBy = "userNo", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<CommunityPost> communityPost; 
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<ItemReview> review;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<Product> items;
	
}