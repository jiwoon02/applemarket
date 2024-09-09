package com.apple.client.community.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import com.apple.location.domain.Location;
import com.apple.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
@Table(name = "Community_Post")
@SequenceGenerator(name = "Community_Post_generator", sequenceName = "Community_Post_seq", initialValue = 1, allocationSize = 1)
public class CommunityPost {

	// 게시글 ID
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Community_Post_generator")
	private long communityPostID;

	// 회원 ID
	@ManyToOne
	@JoinColumn(name = "user_no", nullable = false)
	private User userNo;  // User 객체를 외래 키로 매핑

	// 위치 ID
	@ManyToOne
	@JoinColumn(name = "location_id", nullable = false)
	private Location location;  // Location 객체를 외래 키로 매핑

	// 게시글 제목
	@Column(name = "Community_Title", length = 1000, nullable = false)
	private String communityTitle;

	// 게시글 내용
	@Column(name = "Community_Content", length = 10000, nullable = false)
	private String communityContent;

	// 게시글 이미지 (BLOB)
	@Lob
	@Column(name = "Community_Image", columnDefinition = "BLOB")
	private byte[] communityImage;

	// 게시글 작성일 (null 허용, null일 경우 현재시간 설정)
	@Column(name = "Community_RegDate", updatable = false)
	private LocalDateTime communityRegDate;

	// 게시글 조회수
	@Column(name = "Community_Count")
	@ColumnDefault("0")
	private int communityCount;

	// 엔티티가 처음 저장되기 전에 실행됨
	@PrePersist
	protected void onCreate() {
		if (this.communityRegDate == null) {
			this.communityRegDate = LocalDateTime.now();
		}
	}
}
