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

    // 게시글 전체 조회 (Pageable을 사용하여 페이징 처리)
    Page<CommunityPost> findAllPosts(Pageable pageable);

    // 특정 locationID에 따른 게시글 조회 (Pageable을 사용하여 페이징 처리)
    Page<CommunityPost> getPostsByLocationID(Long locationID, Pageable pageable);

    //이름이나 제목으로 검색 (Pageable을 사용하여 페이징 처리)
	Page<CommunityPost> searchPostsByUserNameOrTitle(String query, Pageable pageable);
}
