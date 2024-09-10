package com.apple.client.community.repository;

import com.apple.client.community.domain.CommunityPost;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Base64;
import java.util.Optional;

@SpringBootTest
public class CommunityTest {

    @Autowired
    private CommunityPostRepository communityPostRepository;

    @Test
    public void testRetrieveImageForPostId() {
        // communityPostID가 261인 데이터를 조회
        Long postId = 261L;  // 게시글 ID 261번
        
        // 해당 게시글을 데이터베이스에서 조회
        Optional<CommunityPost> postOptional = communityPostRepository.findById(postId);
        
        // 게시글이 존재하는지 확인
        Assertions.assertTrue(postOptional.isPresent(), "게시글을 찾을 수 없습니다: ID = " + postId);
        
        // 게시글을 가져옴
        CommunityPost post = postOptional.get();
        
        // 이미지 데이터 가져옴
        byte[] imageBytes = post.getCommunityImage();
        
        // 이미지 데이터가 null이 아닌지 확인
        Assertions.assertNotNull(imageBytes, "이미지 데이터가 null입니다.");
        
        // 이미지 데이터의 길이를 확인하여 실제 이미지가 존재하는지 확인
        Assertions.assertTrue(imageBytes.length > 0, "이미지 데이터가 비어 있습니다.");
        
        // 이미지 데이터를 Base64로 인코딩하여 출력
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        System.out.println("Base64로 인코딩된 이미지 데이터: " + base64Image);
        
        // 원하는 경우 Base64 문자열의 길이도 출력 가능
        System.out.println("Base64 인코딩된 데이터 길이: " + base64Image.length());
    }
}
