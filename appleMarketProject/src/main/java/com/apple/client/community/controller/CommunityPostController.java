package com.apple.client.community.controller;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    // 생성자를 통해 서비스 주입
    public CommunityPostController(CommunityService communityService, 
                                   CommunityCommentService communityCommentService,
                                   UserRepository userRepository,
                                   UserService userService) {
        this.communityService = communityService;
        this.communityCommentService = communityCommentService;
        this.userRepository = userRepository;
        this.userService = userService;
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
        	        post.getCommunityPostID(),  // 게시글 ID 추가
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
        	        post.getCommunityPostID(),  // 게시글 ID 추가
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
        // 페이징 및 정렬 설정 (최신순)
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "communityRegDate"));

        // 제목으로 검색
        Page<CommunityPost> posts = communityService.searchPostsByTitle(query, pageable);

        // 검색 결과를 모델에 추가
        model.addAttribute("posts", posts.getContent());
        model.addAttribute("currentPage", posts.getNumber());
        model.addAttribute("totalPages", posts.getTotalPages());
        model.addAttribute("query", query);

        return "community/communityPostList"; // 검색 결과를 보여줄 뷰 페이지
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

            // JWT가 없을 경우에도 페이지를 보여줌
            Long loggedInUserNo = null;
            boolean isAuthor = false;

            // JWT가 있을 경우만 사용자 정보 확인
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

            // 작성자인지 여부 전달
            model.addAttribute("isAuthor", isAuthor);
            model.addAttribute("comments", communityCommentService.findCommentsByPostId(postId, null)); // 댓글 리스트 추가

            return "community/communityPostDetail";
        } else {
            return "redirect:/community/communityPostList"; // 게시글이 없을 경우 목록으로 리다이렉트
        }
    }


    // 게시글 작성 폼으로 이동
    @GetMapping("/communityInsertForm")
    public String showCommunityPostInsertForm(Model model) {
        model.addAttribute("communityPost", new CommunityPost());
        return "community/communityPostInsertForm";
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

    //게시글 수정
    @PostMapping("/posts/{postId}/update")
    public String updateCommunityPost(@PathVariable Long postId,
                                      @RequestParam("communityTitle") String communityTitle,
                                      @RequestParam("communityContent") String communityContent,
                                      @RequestParam(value = "communityImage", required = false) MultipartFile communityImage,
                                      RedirectAttributes redirectAttributes) {
        try {
            // 기존 게시글을 DB에서 가져옴
            Optional<CommunityPost> existingPostOptional = communityService.findPostById(postId);

            if (existingPostOptional.isPresent()) {
                CommunityPost existingPost = existingPostOptional.get();

                // 제목과 내용 업데이트
                existingPost.setCommunityTitle(communityTitle);
                existingPost.setCommunityContent(communityContent);

                // 이미지 처리
                if (communityImage != null && !communityImage.isEmpty()) {
                    // 새 이미지가 있으면 이미지를 업데이트
                    byte[] imageBytes = communityImage.getBytes();
                    existingPost.setCommunityImage(imageBytes);
                } else if (communityImage == null || communityImage.isEmpty()) {
                    // 이미지를 변경하지 않을 경우 기존 이미지를 유지
                    if (existingPost.getCommunityImage() != null) {
                        // 이미지가 이미 있는 경우는 아무 작업을 하지 않음
                        System.out.println("기존 이미지를 유지합니다.");
                    } else {
                        // 기존 이미지가 없을 경우 null로 설정
                        existingPost.setCommunityImage(null);
                    }
                }

                // 서비스 호출하여 게시글 업데이트 처리
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

    //게시글 삭제 (GET 방식으로 처리)
    @GetMapping("/posts/{postId}/delete")
    public String deleteCommunityPost(@PathVariable Long postId) {
        communityService.deletePost(postId);
        return "redirect:/community/communityPostList";
    }

    //게시글 신고
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
            communityService.reportPost(postId, userNo, reason); // 신고 처리 서비스 호출
            return "신고가 접수되었습니다.";
        } else {
            return "게시글을 찾을 수 없습니다.";
        }
    }

}
