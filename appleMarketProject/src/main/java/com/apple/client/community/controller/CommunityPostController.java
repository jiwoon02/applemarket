package com.apple.client.community.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.apple.client.communityComment.service.CommunityCommentService;
import com.apple.client.community.domain.CommunityPost;
import com.apple.client.community.service.CommunityService;

@Controller
@RequestMapping("/community")
public class CommunityPostController {

    private final CommunityService communityService;
    private final CommunityCommentService communityCommentService;

    // 생성자를 통해 서비스 주입
    public CommunityPostController(CommunityService communityService, CommunityCommentService communityCommentService) {
        this.communityService = communityService;
        this.communityCommentService = communityCommentService;
    }

    // 게시글 리스트 페이지를 반환
    @GetMapping("/communityPostList")
    public String showCommunityPostListPage(Model model) {
        return "community/communityPostList";
    }

    // 무한 스크롤용 게시글 리스트 API
    @GetMapping("/api/communityPostList")
    public ResponseEntity<Page<CommunityPost>> getAllCommunityPostsApi(@RequestParam(defaultValue = "0") int page, 
                                                                       @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("communityPostID").ascending());
        Page<CommunityPost> posts = communityService.findAllPosts(pageable);
        return ResponseEntity.ok(posts);
    }

    // 게시글 상세 페이지로 이동 및 조회수 증가
    @GetMapping("/communityPostDetail/{postId}")
    public String showCommunityPostDetail(@PathVariable Long postId, Model model) {
        Optional<CommunityPost> post = communityService.findPostById(postId);
        if (post.isPresent()) {
            CommunityPost existingPost = post.get();
            // 조회수 증가
            existingPost.setCommunityCount(existingPost.getCommunityCount() + 1);
            communityService.updatePost(postId, existingPost);

            model.addAttribute("communityPost", existingPost);
            model.addAttribute("comments", communityCommentService.findCommentsByPostId(postId)); // 댓글 리스트 추가
            return "community/communityPostDetail";
        } else {
            return "redirect:/community/communityPostList";
        }
    }

    // 게시글 작성 폼으로 이동
    @GetMapping("/communityInsertForm")
    public String showCommunityPostInsertForm(Model model) {
        model.addAttribute("communityPost", new CommunityPost());
        return "community/communityPostInsertForm"; // community/communityPostInsertForm.html 템플릿으로 반환
    }

    // 게시글 생성
    @PostMapping("/posts")
    public String createCommunityPost(@ModelAttribute CommunityPost post) {
        communityService.createPost(post);
        return "redirect:/community/communityPostList?locationID=" + post.getLocationID(); // 생성 후 목록으로 리다이렉트
    }

    // 게시글 수정/삭제 폼으로 이동
    @GetMapping("/communityUpdateForm/{postId}")
    public String showCommunityPostUpdateForm(@PathVariable Long postId, Model model) {
        Optional<CommunityPost> post = communityService.findPostById(postId);
        if (post.isPresent()) {
            model.addAttribute("communityPost", post.get());
            return "community/communityPostUpdateForm"; // community/communityPostUpdateForm.html 템플릿으로 반환
        } else {
            return "redirect:/community/communityPostList"; // 게시글이 없으면 목록으로 리다이렉트
        }
    }

    // 게시글 수정
    @PostMapping("/posts/{postId}/update")
    public String updateCommunityPost(@PathVariable Long postId, @ModelAttribute CommunityPost postDetails) {
        communityService.updatePost(postId, postDetails);
        return "redirect:/community/communityPostDetail/" + postDetails.getCommunityPostID(); // 수정 후 해당 게시글의 상세 페이지로 리다이렉트
    }

    // 게시글 삭제
    @PostMapping("/posts/{postId}/delete")
    public String deleteCommunityPost(@PathVariable Long postId) {
        communityService.deletePost(postId);
        return "redirect:/community/communityPostList"; // 삭제 후 목록으로 리다이렉트
    }
}
