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

    @Override
    public Optional<CommunityPost> findPostById(Long postId) {
        return communityPostRepository.findById(postId);
    }

    @Override
    public CommunityPost createPost(CommunityPost post) {
        return communityPostRepository.save(post);
    }

    @Override
    public CommunityPost updatePost(Long postId, CommunityPost postDetails) {
        Optional<CommunityPost> existingPost = communityPostRepository.findById(postId);
        if (existingPost.isPresent()) {
            CommunityPost postToUpdate = existingPost.get();
            postToUpdate.setCommunityTitle(postDetails.getCommunityTitle());
            postToUpdate.setCommunityContent(postDetails.getCommunityContent());
            // 필요한 필드 추가
            return communityPostRepository.save(postToUpdate);
        }
        return null;
    }

    @Override
    public void deletePost(Long postId) {
        communityPostRepository.deleteById(postId);
    }

    // 전체 게시글 조회 (Pageable 사용)
    @Override
    public Page<CommunityPost> findAllPosts(Pageable pageable) {
        return communityPostRepository.findAll(pageable);
    }

    // 특정 locationID에 따른 게시글 조회 (Pageable 사용)
    @Override
    public Page<CommunityPost> getPostsByLocationID(Long locationID, Pageable pageable) {
        return communityPostRepository.findByLocation_LocationID(locationID, pageable);
    }

    @Override
    public Page<CommunityPost> searchPostsByUserNameOrTitle(String query, Pageable pageable) {
        // 사용자 이름이나 커뮤니티 제목에 따라 검색
        return communityPostRepository.findByUserNo_UserNameContainingOrCommunityTitleContaining(query, query, pageable);
    }
}
