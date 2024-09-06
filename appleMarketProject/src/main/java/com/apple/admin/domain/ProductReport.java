package com.apple.admin.domain;


import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UpdateTimestamp;

import com.apple.product.domain.Product;
import com.apple.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table (name = "apple_productReport")
public class ProductReport {
	
	@Id
	@Column (name = "product_report_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_report_generator")
	private Long productReportID;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	
	@ManyToOne
	@JoinColumn(name = "user_no")
	private User user;
	
	@Column
	private String reportContent;
	
	@UpdateTimestamp
	@Column(name = "product_report_reg_date")
	@ColumnDefault(value = "sysdate")
	private LocalDateTime productReportRegDate;

	 @Override
	    public String toString() {
	        return "ProductReport{" +
	                "productReportID=" + productReportID +
	                ", reportContent='" + reportContent + '\'' +
	                ", productID=" + (product != null ? product.getProductID() : null) +
	                ", userID=" + (user != null ? user.getUserNo() : null) +
	                '}';
	    }

	
}
