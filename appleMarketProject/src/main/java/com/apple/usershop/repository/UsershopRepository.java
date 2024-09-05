package com.apple.usershop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apple.usershop.domain.Usershop;

public interface UsershopRepository extends JpaRepository<Usershop, Long>{
	List<Usershop> findByUser_UserNo(Long userNo);
	
	// shopId를 기준으로 Usershop 엔티티를 조회하는 메서드 추가
    Optional<Usershop> findByShopId(Long shopId);
    
    // 특정 shopId가 존재하는지 확인하는 메서드
    boolean existsByShopId(Long shopId);
    
    // userNo를 기준으로 Usershop 삭제
    void deleteByUser_UserNo(Long userNo);
}
