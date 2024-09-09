package com.apple.client.community.dto;

import org.springframework.web.multipart.MultipartFile;

import groovy.transform.ToString;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor 
public class CommunityPostDto {
    private Long communityPostID;  // 게시글 ID 추가
    private String communityTitle;
    private String communityContent;
    private MultipartFile communityImage; // 이미지 파일 처리용
    private String userName;
    private int communityCount;
    private String communityRegDate;
    
    public CommunityPostDto(String communityTitle, String communityContent, MultipartFile communityImage, String userName, int communityCount, String communityRegDate) {
        this.communityTitle = communityTitle;
        this.communityContent = communityContent;
        this.communityImage = communityImage;
        this.userName = userName;
        this.communityCount = communityCount;
        this.communityRegDate = communityRegDate;
    }
    
    public CommunityPostDto(String communityTitle, String communityContent, String userName, int communityCount, String communityRegDate) {
        this.communityTitle = communityTitle;
        this.communityContent = communityContent;
        this.userName = userName;
        this.communityCount = communityCount;
        this.communityRegDate = communityRegDate;
    }
    
    // 기존 생성자 외에 communityPostID를 포함하는 생성자 추가
    public CommunityPostDto(Long communityPostID, String communityTitle, String communityContent, 
                            String userName, int communityCount, String communityRegDate) {
        this.communityPostID = communityPostID;
        this.communityTitle = communityTitle;
        this.communityContent = communityContent;
        this.userName = userName;
        this.communityCount = communityCount;
        this.communityRegDate = communityRegDate;
    }

}
