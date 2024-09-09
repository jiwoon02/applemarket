package com.apple.client.communityComment.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.apple.client.communityComment.domain.CommunityComment;
import com.apple.client.communityComment.dto.CommunityCommentDto;
import com.apple.client.communityComment.repository.CommunityCommentRepository;

@Service
public class CommunityCommentServiceImpl implements CommunityCommentService {

    private final CommunityCommentRepository communityCommentRepository;

    public CommunityCommentServiceImpl(CommunityCommentRepository communityCommentRepository) {
        this.communityCommentRepository = communityCommentRepository;
    }

    @Override
    public List<CommunityCommentDto> findCommentsByPostId(Long postId, Long loggedInUserNo) {
    	// commentID를 기준으로 내림차순 정렬
        List<CommunityComment> comments = communityCommentRepository.findByCommunityPost_CommunityPostID(postId, Sort.by(Sort.Direction.DESC, "commentID"));
        return comments.stream()
                .map(comment -> convertToDto(comment, loggedInUserNo)) // DTO 변환 메서드 호출
                .collect(Collectors.toList());
    }

    @Override
    public CommunityComment createComment(CommunityComment comment) {
        return communityCommentRepository.save(comment);
    }

    @Override
    public CommunityComment updateComment(Long commentId, CommunityComment commentDetails, Long userNo) {
        Optional<CommunityComment> optionalComment = communityCommentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            CommunityComment existingComment = optionalComment.get();
            if (existingComment.getUserNo().getUserNo().equals(userNo)) {
                existingComment.setCommentContent(commentDetails.getCommentContent());
                return communityCommentRepository.save(existingComment);
            } else {
                throw new SecurityException("댓글 작성자만 수정할 수 있습니다.");
            }
        } else {
            throw new IllegalArgumentException("해당 댓글을 찾을 수 없습니다.");
        }
    }

    @Override
    public void deleteComment(Long commentId, Long userNo) {
        Optional<CommunityComment> optionalComment = communityCommentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            CommunityComment existingComment = optionalComment.get();
            if (existingComment.getUserNo().getUserNo().equals(userNo)) {
                communityCommentRepository.deleteById(commentId);
            } else {
                throw new SecurityException("댓글 작성자만 삭제할 수 있습니다.");
            }
        } else {
            throw new IllegalArgumentException("해당 댓글을 찾을 수 없습니다.");
        }
    }

    @Override
    public Optional<CommunityComment> findCommentById(Long commentId) {
        return communityCommentRepository.findById(commentId);
    }

    @Override
    public CommunityCommentDto convertToDto(CommunityComment comment, Long loggedInUserNo) {
        CommunityCommentDto dto = new CommunityCommentDto(
            comment.getCommentID(),
            comment.getCommentContent(),
            comment.getCommentRegDate(),
            comment.getUserNo().getUserName(),  // User 테이블에서 작성자의 이름을 가져옴
            comment.getUserNo().getUserNo(),    // User 테이블에서 작성자의 번호를 가져옴
            comment.getCommunityPost().getCommunityPostID()  // 게시글 ID
        );

        // 로그인한 사용자와 댓글 작성자가 동일한지 확인하여 DTO에 설정
        dto.setCommentOwner(loggedInUserNo != null && comment.getUserNo().getUserNo().equals(loggedInUserNo));
        return dto;
    }
}
