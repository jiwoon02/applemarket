package com.apple.client.community.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.apple.client.community.domain.CommunityPost;
import com.apple.client.community.repository.CommunityPostRepository;

@Service
public class CommunityServiceImpl implements CommunityService {

    private final CommunityPostRepository communityPostRepository;

    public CommunityServiceImpl(CommunityPostRepository communityPostRepository) {
        this.communityPostRepository = communityPostRepository;
    }
    
    //게시글특정 id 로 조회
    @Override
    public Optional<CommunityPost> findPostById(Long postId) {
        return communityPostRepository.findById(postId);
    }

    //게시글 새성
    @Override
    public void createPost(CommunityPost post) { // 반환 타입을 void로 변경
        communityPostRepository.save(post);
    }
    
    //게시글 업데이트
    @Override
    public void updatePost(Long postId, CommunityPost postDetails) {
        Optional<CommunityPost> existingPost = communityPostRepository.findById(postId);
        if (existingPost.isPresent()) {
            CommunityPost postToUpdate = existingPost.get();
            postToUpdate.setCommunityTitle(postDetails.getCommunityTitle());
            postToUpdate.setCommunityContent(postDetails.getCommunityContent());

            // 이미지가 포함된 경우에 처리
            if (postDetails.getCommunityImage() != null) {
                postToUpdate.setCommunityImage(postDetails.getCommunityImage());
            }

            communityPostRepository.save(postToUpdate); // 업데이트 후 저장
        }
    }
    
    //게시글 상세 조회
    @Override
    public void deletePost(Long postId) {
        communityPostRepository.deleteById(postId);
    }
    
    //게시글 신고
    @Override
    public void reportPost(Long postId, Long userNo, String reason) {
        // 신고 로직 처리 (예: 신고 내용을 DB에 저장하거나 관리자가 확인할 수 있도록 처리)
        System.out.println("Post ID: " + postId + ", User No: " + userNo + ", Reason: " + reason);
        // 실제로는 DB에 저장하는 로직을 구현할 수 있습니다.
    }

    // 전체 게시글 조회 (Pageable 사용)
    @Override
    public Page<CommunityPost> findAllPosts(Pageable pageable) {
        return communityPostRepository.findAll(pageable);
    }

    // 특정 locationID에 따른 게시글 조회 (Pageable 사용)
    @Override
    public Page<CommunityPost> findPostsByLocationID(Long locationID, Pageable pageable) { // 메서드명 수정
        return communityPostRepository.findByLocation_LocationID(locationID, pageable);
    }
    
    //게시글 검색
    @Override
    public Page<CommunityPost> searchPostsByUserNameOrTitle(String query, Pageable pageable) {
        // 사용자 이름이나 커뮤니티 제목에 따라 검색
        return communityPostRepository.findByUserNo_UserNameContainingOrCommunityTitleContaining(query, query, pageable);
    }
}
