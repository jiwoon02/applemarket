package com.apple.client.community.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.apple.client.community.domain.CommunityPost;
import com.apple.user.domain.User;

public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {
    
    // 특정 LocationID에 해당하는 게시글 조회 (Pageable을 사용하여 페이징 처리)
    Page<CommunityPost> findByLocation_LocationID(Long locationID, Pageable pageable);

    // 전체 게시글 조회 (Pageable을 사용하여 페이징 처리)
    Page<CommunityPost> findAll(Pageable pageable);
    
    // 특정 사용자(userNo)의 게시글을 모두 찾기
    List<CommunityPost> findByUserNo(User userNo);
    
    // 페이징 처리
    Page<CommunityPost> findByLocationID(Location location, Pageable pageable);
    
    // userNo를 기준으로 Usershop 삭제
    void deleteByUserNo_UserNo(Long userNo);
    //userName이나 communityTitle을 검색 (Pageable을 사용하여 페이징 처리)
    Page<CommunityPost> findByUserNo_UserNameContainingOrCommunityTitleContaining(String userName, String communityTitle, Pageable pageable);
}
