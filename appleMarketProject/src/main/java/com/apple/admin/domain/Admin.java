package com.apple.admin.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "apple_admin")
@Builder
public class Admin {
	
	@Id
	private String adminName;
	
	@Column(name = "admin_Passwd",  length = 15, nullable = false)
	private String adminPasswd;
	
	@Column(name = "admin_role", nullable = false)
	private String adminRole; // 사용자 권한 (예: ADMIN)

	@PrePersist
	    protected void onCreate() {
	        this.adminRole = "ADMIN";
	    }
}
