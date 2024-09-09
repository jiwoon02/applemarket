package com.apple.client.community.repository;

import com.apple.client.community.domain.CommunityPost;
import com.apple.location.domain.Location;
import com.apple.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class CommunityTest {

    @Autowired
    private CommunityPostRepository communityPostRepository;

    @Test
    public void insertTestPosts() {
        // 가상 User 객체 생성 (UserNo가 1인 사용자)
        User user = new User();
        user.setUserNo(21L);

        // 가상 Location 객체 생성 (locationID가 1114015800인 위치)
        Location location = new Location();
        location.setLocationID(1174010900L);

        // 100개의 테스트 게시글 생성 및 저장
        for (int i = 1; i <= 100; i++) {
            CommunityPost post = new CommunityPost();
            post.setCommunityTitle("테스트 제목 " + i);
            post.setCommunityContent("이것은 테스트 내용입니다. 게시글 번호: " + i);
            post.setUserNo(user);
            post.setLocation(location);
            post.setCommunityRegDate(LocalDateTime.now());
            post.setCommunityCount(0); // 초기 조회수 설정

            communityPostRepository.save(post);
        }

        System.out.println("100개의 테스트 게시글이 성공적으로 입력되었습니다.");
    }
}
