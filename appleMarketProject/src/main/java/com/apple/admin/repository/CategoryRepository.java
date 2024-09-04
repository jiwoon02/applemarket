package com.apple.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apple.category.domain.Category;


public interface CategoryRepository extends JpaRepository<Category, String> {

}
