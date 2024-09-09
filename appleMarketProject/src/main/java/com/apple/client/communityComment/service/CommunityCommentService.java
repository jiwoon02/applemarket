package com.apple.client.communityComment.service;

import java.util.List;
import java.util.Optional;

import com.apple.client.communityComment.domain.CommunityComment;
import com.apple.client.communityComment.dto.CommunityCommentDto;

public interface CommunityCommentService {

    // 댓글 입력
    CommunityComment createComment(CommunityComment comment);

    // 댓글 수정
    CommunityComment updateComment(Long commentId, CommunityComment commentDetails, Long userNo);

    // 댓글 삭제
    void deleteComment(Long commentId, Long userNo);

    // 댓글 ID로 댓글 찾기
    Optional<CommunityComment> findCommentById(Long commentId);

    // 게시글 ID로 댓글 목록 찾기 (DTO 반환)
    List<CommunityCommentDto> findCommentsByPostId(Long postId, Long loggedInUserNo);

    // CommunityComment -> DTO 변환 메서드 선언
    CommunityCommentDto convertToDto(CommunityComment comment, Long loggedInUserNo);

}
