package com.apple.admin.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apple.admin.domain.Admin;



public interface AdminRepository extends JpaRepository<Admin, String> {

	Admin findByAdminName(String adminName);

	
}
