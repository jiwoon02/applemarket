package com.apple.client.community.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.apple.client.community.domain.CommunityPost;

public interface CommunityService {

    // 특정 게시글 ID로 조회
    Optional<CommunityPost> findPostById(Long postId);

    // 새 게시글 생성
    void createPost(CommunityPost post); // 반환 타입을 void로 변경하여 일관성 유지

    // 게시글 수정
    void updatePost(Long postId, CommunityPost postDetails); // 반환 타입을 void로 통일

    // 게시글 삭제
    void deletePost(Long postId);
    
    //게시글 신고
    void reportPost(Long postId, Long userNo, String reason);

    // 게시글 전체 조회 (Pageable을 사용하여 페이징 처리)
    Page<CommunityPost> findAllPosts(Pageable pageable); // 모든 게시글 조회

    // 특정 locationID에 따른 게시글 조회 (Pageable을 사용하여 페이징 처리)
    Page<CommunityPost> findPostsByLocationID(Long locationID, Pageable pageable); // 메서드명 변경

    // 이름이나 제목으로 검색 (Pageable을 사용하여 페이징 처리)
	Page<CommunityPost> searchPostsByUserNameOrTitle(String query, Pageable pageable); // 검색 기능 유지
}
