package com.apple.client.community.controller;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Base64;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.apple.client.communityComment.service.CommunityCommentService;
import com.apple.location.domain.Location;
import com.apple.client.community.domain.CommunityPost;
import com.apple.client.community.dto.CommunityPostDto;
import com.apple.client.community.repository.CommunityPostRepository;
import com.apple.client.community.service.CommunityService;
import com.apple.user.domain.User;
import com.apple.user.repository.UserRepository;
import com.apple.user.service.UserService;

@Controller
@RequestMapping("/community")
public class CommunityPostController {

    private final CommunityService communityService;
    private final CommunityCommentService communityCommentService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final CommunityPostRepository communityPostRepository;

    // 생성자를 통해 서비스 주입
    public CommunityPostController(CommunityService communityService, 
                                   CommunityCommentService communityCommentService,
                                   UserRepository userRepository,
                                   UserService userService,
                                   CommunityPostRepository communityPostRepository) {
        this.communityService = communityService;
        this.communityCommentService = communityCommentService;
        this.userRepository = userRepository;
        this.userService = userService;
        this.communityPostRepository = communityPostRepository;
    }

    // 게시글 리스트 페이지를 반환
    @GetMapping("/communityPostList")
    public String showCommunityPostListPage(@CookieValue(value = "JWT", required = false) String token, Model model) {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "communityRegDate"));
        Page<CommunityPost> posts;

        if (token == null || token.isEmpty()) {
            posts = communityService.findAllPosts(pageable);
        } else {
            Long userNo = userService.getUserNo(token);
            Long locationID = userService.getLocationIDByUserNo(userNo);
            posts = communityService.findPostsByLocationID(locationID, pageable);
        }

        List<CommunityPostDto> postDtos = posts.getContent().stream()
        	    .map(post -> new CommunityPostDto(
        	        post.getCommunityPostID(),  
        	        post.getCommunityTitle(),
        	        post.getCommunityContent(),
        	        post.getUserNo().getUserName(),
        	        post.getCommunityCount(),
        	        post.getCommunityRegDate().toString()
        	    ))
        	    .collect(Collectors.toList());

        model.addAttribute("posts", postDtos);
        return "community/communityPostList";
    }

    // 무한 스크롤로 게시글 데이터를 제공하는 API
    @GetMapping("/api/communityPostList")
    @ResponseBody
    public Map<String, Object> getCommunityPosts(@RequestParam(defaultValue = "0") int offset,
                                                 @RequestParam(defaultValue = "10") int limit,
                                                 @CookieValue(value = "JWT", required = false) String token) {
        Pageable pageable = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "communityRegDate"));
        Page<CommunityPost> posts;

        if (token == null || token.isEmpty()) {
            posts = communityService.findAllPosts(pageable);
        } else {
            Long userNo = userService.getUserNo(token);
            Long locationID = userService.getLocationIDByUserNo(userNo);
            posts = communityService.findPostsByLocationID(locationID, pageable);
        }

        List<CommunityPostDto> postDtos = posts.getContent().stream()
        	    .map(post -> new CommunityPostDto(
        	        post.getCommunityPostID(),  
        	        post.getCommunityTitle(),
        	        post.getCommunityContent(),
        	        post.getUserNo().getUserName(),
        	        post.getCommunityCount(),
        	        post.getCommunityRegDate().toString()
        	    ))
        	    .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("content", postDtos);
        response.put("offset", offset);
        response.put("limit", limit);

        return response;
    }

    // 검색 기능 추가
    @GetMapping("/communityPostList/search")
    public String searchPosts(@RequestParam("query") String query, 
                              @RequestParam(defaultValue = "0") int page, 
                              @RequestParam(defaultValue = "10") int size,
                              Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "communityRegDate"));

        Page<CommunityPost> posts = communityService.searchPostsByTitle(query, pageable);

        model.addAttribute("posts", posts.getContent());
        model.addAttribute("currentPage", posts.getNumber());
        model.addAttribute("totalPages", posts.getTotalPages());
        model.addAttribute("query", query);

        return "community/communityPostList";
    }

    // 게시글 생성
    @PostMapping("/posts")
    public String createCommunityPost(@CookieValue(value = "JWT") String token, 
                                      @ModelAttribute CommunityPostDto postDto) throws IOException {
        if (token == null || token.isEmpty()) {
            return "redirect:/community/communityPostList";
        }

        Long userNo = userService.getUserNo(token);
        Optional<User> optionalUser = userRepository.findByUserNo(userNo);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Location location = user.getLocation();

            CommunityPost post = new CommunityPost();
            post.setUserNo(user);
            post.setLocation(location);
            post.setCommunityTitle(postDto.getCommunityTitle());
            post.setCommunityContent(postDto.getCommunityContent());

            if (!postDto.getCommunityImage().isEmpty()) {
                post.setCommunityImage(postDto.getCommunityImage().getBytes());
            }

            communityService.createPost(post);
            return "redirect:/community/communityPostList?locationID=" + location.getLocationID();
        }
        return "redirect:/community/communityPostList";
    }

    // 게시글 상세 페이지로 이동 및 조회수 증가
    @GetMapping("/communityPostDetail/{postId}")
    public String showCommunityPostDetail(@PathVariable Long postId, 
                                          @CookieValue(value = "JWT", required = false) String token, 
                                          Model model) {
        Optional<CommunityPost> post = communityService.findPostById(postId);
        
        if (post.isPresent()) {
            CommunityPost existingPost = post.get();
            existingPost.setCommunityCount(existingPost.getCommunityCount() + 1); // 조회수 증가
            communityService.updatePost(postId, existingPost, false);

            model.addAttribute("communityPost", existingPost);

            Long loggedInUserNo = null;
            boolean isAuthor = false;

            if (token != null && !token.isEmpty()) {
                try {
                    loggedInUserNo = userService.getUserNo(token);
                    if (loggedInUserNo != null) {
                        isAuthor = loggedInUserNo.equals(existingPost.getUserNo().getUserNo());
                    }
                } catch (Exception e) {
                    System.out.println("Invalid token: " + e.getMessage());
                }
            }

            model.addAttribute("isAuthor", isAuthor);
            model.addAttribute("comments", communityCommentService.findCommentsByPostId(postId, null)); // 댓글 리스트 추가

            return "community/communityPostDetail";
        } else {
            return "redirect:/community/communityPostList";
        }
    }
    
    // 게시글 수정 폼으로 이동
    @GetMapping("/communityUpdateForm/{postId}")
    public String showCommunityPostUpdateForm(@PathVariable Long postId, Model model) {
        Optional<CommunityPost> post = communityService.findPostById(postId);
        if (post.isPresent()) {
            model.addAttribute("communityPost", post.get());
            return "community/communityPostUpdateForm";
        } else {
            return "redirect:/community/communityPostList";
        }
    }
    
    // 게시글 수정
    @PostMapping("/posts/{postId}/update")
    public String updateCommunityPost(@PathVariable Long postId,
                                      @RequestParam("communityTitle") String communityTitle,
                                      @RequestParam("communityContent") String communityContent,
                                      @RequestParam(value = "communityImage", required = false) MultipartFile communityImage,
                                      RedirectAttributes redirectAttributes) {
        try {
            Optional<CommunityPost> existingPostOptional = communityService.findPostById(postId);

            if (existingPostOptional.isPresent()) {
                CommunityPost existingPost = existingPostOptional.get();
                existingPost.setCommunityTitle(communityTitle);
                existingPost.setCommunityContent(communityContent);

                if (communityImage != null && !communityImage.isEmpty()) {
                    byte[] imageBytes = communityImage.getBytes();
                    existingPost.setCommunityImage(imageBytes);
                } else if (communityImage == null || communityImage.isEmpty()) {
                    if (existingPost.getCommunityImage() != null) {
                        System.out.println("기존 이미지를 유지합니다.");
                    } else {
                        existingPost.setCommunityImage(null);
                    }
                }

                communityService.updatePost(postId, existingPost, communityImage != null);

                redirectAttributes.addFlashAttribute("message", "게시글이 성공적으로 수정되었습니다.");
                return "redirect:/community/communityPostDetail/" + postId;
            } else {
                redirectAttributes.addFlashAttribute("message", "게시글을 찾을 수 없습니다.");
                return "redirect:/community/communityPostList";
            }
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "파일 업로드 중 오류가 발생했습니다.");
            return "redirect:/community/communityPostDetail/" + postId;
        }
    }

    // 게시글 신고
    @PostMapping("/posts/{postId}/report")
    @ResponseBody
    public String reportPost(@PathVariable Long postId, @RequestParam("reason") String reason, 
                             @CookieValue(value = "JWT", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return "로그인이 필요합니다.";
        }

        Long userNo = userService.getUserNo(token);
        Optional<CommunityPost> post = communityService.findPostById(postId);

        if (post.isPresent()) {
            communityService.reportPost(postId, userNo, reason);
            return "신고가 접수되었습니다.";
        } else {
            return "게시글을 찾을 수 없습니다.";
        }
    }
    
    // Base64 이미지 값을 반환하는 메서드 
    @GetMapping("/image/base64/{postId}")
    public ResponseEntity<String> getBase64Image(@PathVariable Long postId) {
        Optional<CommunityPost> postOptional = communityPostRepository.findById(postId);

        if (postOptional.isPresent()) {
            byte[] imageBytes = postOptional.get().getCommunityImage();

            if (imageBytes != null && imageBytes.length > 0) {
                String mimeType = "image/jpeg";  // 기본적으로 JPEG 형식으로 가정

                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                String imageDataUrl = "data:" + mimeType + ";base64," + base64Image;

                return ResponseEntity.ok(imageDataUrl);
            } else {
                return ResponseEntity.ok(null);  // 이미지가 없을 때 null 반환
            }
        }

        return ResponseEntity.notFound().build();
    }
}
