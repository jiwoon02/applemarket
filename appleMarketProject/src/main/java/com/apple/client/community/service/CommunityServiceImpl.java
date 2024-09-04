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

    @Override
    public Page<CommunityPost> findAllPosts(Pageable pageable) {
        return communityPostRepository.findAll(pageable);
    }
}
