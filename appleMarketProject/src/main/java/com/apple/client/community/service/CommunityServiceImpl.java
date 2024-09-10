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
    
    // 특정 게시글 ID로 조회
    @Override
    public Optional<CommunityPost> findPostById(Long postId) {
        return communityPostRepository.findById(postId);
    }

    // 게시글 생성
    @Override
    public void createPost(CommunityPost post) {
        communityPostRepository.save(post);
    }
    
    // 게시글 업데이트
    @Override
    public void updatePost(Long postId, CommunityPost postDetails, boolean isImageUpdated) {
        Optional<CommunityPost> existingPost = communityPostRepository.findById(postId);
        if (existingPost.isPresent()) {
            CommunityPost postToUpdate = existingPost.get();
            postToUpdate.setCommunityTitle(postDetails.getCommunityTitle());
            postToUpdate.setCommunityContent(postDetails.getCommunityContent());

            // 이미지 처리 로직
            if (isImageUpdated) {
                // 새 이미지가 있으면 업데이트
                postToUpdate.setCommunityImage(postDetails.getCommunityImage());
            } else if (postDetails.getCommunityImage() == null) {
                // 이미지가 없으면 null로 설정
                postToUpdate.setCommunityImage(null);
            } else {
                // 이미지 변경사항이 없으면 기존 이미지 유지
                postToUpdate.setCommunityImage(postToUpdate.getCommunityImage());
            }

            communityPostRepository.save(postToUpdate); // 변경사항 저장
        } else {
            throw new RuntimeException("해당 게시글을 찾을 수 없습니다.");
        }
    }
    
    // 게시글 삭제
    @Override
    public void deletePost(Long postId) {
        communityPostRepository.deleteById(postId);
    }
    
    // 게시글 신고
    @Override
    public void reportPost(Long postId, Long userNo, String reason) {
        System.out.println("Post ID: " + postId + ", User No: " + userNo + ", Reason: " + reason);
    }

    // 전체 게시글 조회
    @Override
    public Page<CommunityPost> findAllPosts(Pageable pageable) {
        return communityPostRepository.findAll(pageable);
    }

    // 특정 LocationID에 따른 게시글 조회
    @Override
    public Page<CommunityPost> findPostsByLocationID(Long locationID, Pageable pageable) {
        return communityPostRepository.findByLocation_LocationID(locationID, pageable);
    }
    
    // 게시글 제목으로 검색
    @Override
    public Page<CommunityPost> searchPostsByTitle(String query, Pageable pageable) {
        return communityPostRepository.searchPostsByTitle(query, pageable);
    }
}
