package com.apple.category.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "category")
public class Category {
	
		@Id
		@Column(name="category_id")
		private String categoryID; //대표키
		
		@Column(name="category_name", nullable = false)
		private String categoryName;

		  @Override
		    public String toString() {
		        return "Category{" +
		                "id=" + categoryID +
		                ", name='" + categoryName + '\'' +
		                '}';
		    }
}
