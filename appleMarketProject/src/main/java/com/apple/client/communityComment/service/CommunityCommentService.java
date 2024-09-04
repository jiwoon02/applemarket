package com.apple.client.communityComment.service;

import java.util.List;

import com.apple.client.communityComment.domain.CommunityComment;

public interface CommunityCommentService {

    // 댓글 입력
    CommunityComment createComment(CommunityComment comment);

    // 댓글 수정
    CommunityComment updateComment(Long commentId, CommunityComment commentDetails);

    // 댓글 삭제
    void deleteComment(Long commentId);

	List<CommunityComment> findCommentsByPostId(Long postId);


}
