package com.apple.product.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.*;
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
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

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
    private Long productID;

    @ManyToOne
    @JoinColumn(name = "user_no")
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
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

    @Column(name = "product_status")
    private String productStatus = "판매 중";

    @CreationTimestamp
    @Column(name = "product_reg_date", nullable = false)
    @ColumnDefault(value="sysdate")
    private LocalDateTime productRegDate;

    @UpdateTimestamp
    @Column(name = "product_update_date")
    private LocalDateTime productUpdateDate;

    @Column(name = "product_visit_count", nullable = false)
    @ColumnDefault(value = "0")
    private int productVisitCount;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImages> productImages = new ArrayList<>();

    @Transient
    private List<String> existingFileNames;

    @Transient
    private List<MultipartFile> files; // 파일 업로드용

    @Transient
    private List<String> deletedImageFilenames; // 삭제할 파일 이름 리스트

}
