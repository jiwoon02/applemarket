package com.apple.client.communityComment.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.apple.client.communityComment.domain.CommunityComment;
import com.apple.client.communityComment.repository.CommunityCommentRepository;

@Service
public class CommunityCommentServiceImpl implements CommunityCommentService {

    private final CommunityCommentRepository communityCommentRepository;

    public CommunityCommentServiceImpl(CommunityCommentRepository communityCommentRepository) {
        this.communityCommentRepository = communityCommentRepository;
    }

    @Override
    public List<CommunityComment> findCommentsByPostId(Long postId) {
        // 게시글 ID에 해당하는 댓글 목록을 반환합니다.
        return communityCommentRepository.findByCommunityPost_CommunityPostID(postId);
    }

    @Override
    public CommunityComment createComment(CommunityComment comment) {
        // 댓글을 생성하여 저장합니다.
        return communityCommentRepository.save(comment);
    }

    @Override
    public CommunityComment updateComment(Long commentId, CommunityComment commentDetails) {
        // 특정 ID의 댓글을 찾고, 업데이트합니다.
        Optional<CommunityComment> optionalComment = communityCommentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            CommunityComment existingComment = optionalComment.get();
            existingComment.setCommentContent(commentDetails.getCommentContent());
            // 필요한 경우 다른 필드도 업데이트 가능합니다.
            return communityCommentRepository.save(existingComment);
        } else {
            // 댓글을 찾지 못한 경우 예외를 발생시키거나 null을 반환할 수 있습니다.
            return null;
        }
    }

    @Override
    public void deleteComment(Long commentId) {
        // 특정 ID의 댓글을 삭제합니다.
        communityCommentRepository.deleteById(commentId);
    }

}
