package com.apple.client.communityComment.domain;

import java.time.LocalDateTime;

import com.apple.client.community.domain.CommunityPost;
import com.apple.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Community_Comment")
@SequenceGenerator(name = "Community_Comment_generator", sequenceName = "Community_Comment_seq", initialValue = 1, allocationSize = 1)
@Getter
@Setter
public class CommunityComment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Community_Comment_generator")
    @Column(name = "comment_ID")
    private Long commentID;

    @ManyToOne
    @JoinColumn(name = "user_no", nullable = false)  // 수정: @JoinColumn 사용
    private User userNo;

    @ManyToOne
    @JoinColumn(name = "communityPostID", nullable = false)
    private CommunityPost communityPost;

    @Column(name = "comment_Content", length = 1000, nullable = false)
    private String commentContent;

    @Column(name = "comment_RegDate")
    private LocalDateTime commentRegDate;

    @PrePersist
    protected void onCreate() {
        this.commentRegDate = LocalDateTime.now();
    }
}
