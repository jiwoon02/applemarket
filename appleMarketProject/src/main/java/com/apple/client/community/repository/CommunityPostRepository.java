package com.apple.client.community.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.apple.client.community.domain.CommunityPost;
import com.apple.location.domain.Location;
import com.apple.user.domain.User;

public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {
    // 특정 위치(locationID)의 게시글을 모두 찾기
    List<CommunityPost> findByLocationID(Location locationID);

    // 특정 사용자(userNo)의 게시글을 모두 찾기
    List<CommunityPost> findByUserNo(User userNo);
    
    // 페이징 처리
    Page<CommunityPost> findByLocationID(Location location, Pageable pageable);
}
