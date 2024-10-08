package com.apple.client.communityComment.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.apple.client.communityComment.domain.CommunityComment;

public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long>{
	
	// 특정 게시글 ID에 해당하는 모든 댓글 조회
    List<CommunityComment> findByCommunityPost_CommunityPostID(Long postId);
    
    // userNo를 기준으로 Usershop 삭제
    void deleteByUserNo_UserNo(Long userNo);
    
    // 내림차순 정렬 추가
    List<CommunityComment> findByCommunityPost_CommunityPostID(Long postId, Sort sort);
    
    @Modifying
    @Query("DELETE FROM CommunityComment cm WHERE cm.communityPost.communityPostID IN :communityPostids")
	void deleteByCommunitycommnet(List<Long> communityPostids);
}
