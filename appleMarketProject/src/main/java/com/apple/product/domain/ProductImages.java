package com.apple.product.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_images")
@SequenceGenerator(name="productImage_generator", sequenceName = "apple_productImage_seq", initialValue = 1, allocationSize = 1)
public class ProductImages {

    @Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "productImage_generator")
    @Column(name = "product_image_id")
    private Long productImageID;
    
	@Column(nullable = false)
	private String filename = ""; //실제 서버에 저장할 파일명 
	
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
}