package com.apple.client.communityComment.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CommunityCommentDto {

    private Long commentID;
    private String commentContent;
    private LocalDateTime commentRegDate;
    private String userName;
    private Long userNo;
    private Long communityPostID;
    
    // 댓글 작성자인지 여부 확인을 위한 필드 추가
    private boolean commentOwner; // 댓글 작성자 여부
    
    // Getter와 Setter 추가
    public boolean isCommentOwner() {
        return commentOwner;
    }

    public void setCommentOwner(boolean commentOwner) {
        this.commentOwner = commentOwner;
    }
    // 생성자
    public CommunityCommentDto(Long commentID, String commentContent, LocalDateTime commentRegDate, 
                               String userName, Long userNo, Long communityPostID) {
        this.commentID = commentID;
        this.commentContent = commentContent;
        this.commentRegDate = commentRegDate;
        this.userName = userName;
        this.userNo = userNo;
        this.communityPostID = communityPostID;
    }
}

