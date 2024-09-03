package com.apple.client.community.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.apple.client.community.domain.CommunityPost;

public interface CommunityService {

    // 특정 게시글 ID로 조회
    Optional<CommunityPost> findPostById(Long postId);

    // 새 게시글 작성
    CommunityPost createPost(CommunityPost post);

    // 게시글 수정
    CommunityPost updatePost(Long postId, CommunityPost postDetails);

    // 게시글 삭제
    void deletePost(Long postId);
    
    // 게시글 전체 조회 (페이징 처리)
    Page<CommunityPost> findAllPosts(Pageable pageable);
}
