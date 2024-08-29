package com.apple.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apple.location.domain.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
	
}
