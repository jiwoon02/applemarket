package com.apple.client.community.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.apple.client.community.domain.CommunityPost;
import com.apple.location.domain.Location;
import com.apple.user.domain.User;

public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {
    
    // 특정 LocationID에 해당하는 게시글 조회 (Pageable을 사용하여 페이징 처리)
    Page<CommunityPost> findByLocation_LocationID(Long locationID, Pageable pageable);

    // 전체 게시글 조회 (Pageable을 사용하여 페이징 처리)
    Page<CommunityPost> findAll(Pageable pageable);
    
    // 특정 사용자(userNo)의 게시글을 모두 찾기
    List<CommunityPost> findByUserNo(User userNo);
    
    // 페이징 처리
    Page<CommunityPost> findByLocation(Location location, Pageable pageable);
    
    // userNo를 기준으로 Usershop 삭제
    void deleteByUserNo_UserNo(Long userNo);
    
    // 특정 검색어가 제목에 포함된 게시글을 페이징 처리하여 조회
    @Query("SELECT p FROM CommunityPost p WHERE p.communityTitle LIKE %:query%")
    Page<CommunityPost> searchPostsByTitle(@Param("query") String query, Pageable pageable);
}
