package com.apple.client.communityComment.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.apple.client.communityComment.domain.CommunityComment;
import com.apple.client.communityComment.dto.CommunityCommentDto;
import com.apple.client.communityComment.service.CommunityCommentService;
import com.apple.user.domain.User;
import com.apple.user.repository.UserRepository;
import com.apple.user.service.UserService;

@Controller
@RequestMapping("/comments")
public class CommunityCommentController {

    @Autowired
    private CommunityCommentService communityCommentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    // 댓글 리스트 구현 (DTO로 변환 후 반환)
    @GetMapping(value = "/all/{postId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<CommunityCommentDto> getCommentList(@PathVariable Long postId, 
                                                    @CookieValue(value = "JWT", required = false) String token) {
        Long loggedInUserNo = null;
        if (token != null && !token.isEmpty()) {
            loggedInUserNo = userService.getUserNo(token);
        }

        // 댓글 리스트 반환 (로그인한 사용자 정보를 함께 전달)
        return communityCommentService.findCommentsByPostId(postId, loggedInUserNo);
    }

    // 댓글 등록 페이지 보여주기
    @GetMapping("/{postId}")
    public String showCommentsPage(@PathVariable Long postId, 
                                   @CookieValue(value = "JWT", required = false) String token, 
                                   Model model) {
        if (token == null || token.isEmpty()) {
            model.addAttribute("userName", "사용자");
        } else {
            Long userNo = userService.getUserNo(token);
            Optional<User> optionalUser = userRepository.findByUserNo(userNo);
            optionalUser.ifPresent(user -> model.addAttribute("userName", user.getUserName()));
        }

        // DTO로 변환된 댓글 리스트를 모델에 추가
        List<CommunityCommentDto> commentDtos = communityCommentService.findCommentsByPostId(postId, null);  // 로그인 여부 없이 호출
        model.addAttribute("comments", commentDtos);

        return "communityComment";
    }

    // 댓글 등록
    @PostMapping(value = "/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String insertComment(@RequestBody CommunityComment comment, 
                                @CookieValue(value = "JWT", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return "로그인이 필요합니다.";
        }

        Long userNo = userService.getUserNo(token);
        Optional<User> optionalUser = userRepository.findByUserNo(userNo);

        if (optionalUser.isPresent()) {
            comment.setUserNo(optionalUser.get());
            CommunityComment createdComment = communityCommentService.createComment(comment);
            return (createdComment != null) ? "SUCCESS" : "FAILURE";
        }

        return "유효하지 않은 사용자입니다.";
    }

    // 댓글 수정
    @PutMapping(value = "/update/{commentId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String updateComment(@PathVariable Long commentId, 
                                @RequestBody CommunityComment commentDetails,
                                @CookieValue(value = "JWT", required = false) String token) {

        Long userNo = userService.getUserNo(token);  // JWT에서 userNo 추출
        Optional<CommunityComment> optionalComment = communityCommentService.findCommentById(commentId);

        if (optionalComment.isPresent()) {
            CommunityComment comment = optionalComment.get();

            // 사용자가 작성자인지 확인
            if (comment.getUserNo().getUserNo().equals(userNo)) {
                communityCommentService.updateComment(commentId, commentDetails, userNo);
                return "SUCCESS";
            } else {
                return "댓글 작성자만 수정할 수 있습니다.";
            }
        }

        return "댓글을 수정할 수 없습니다.";
    }

    // 댓글 삭제
    @DeleteMapping(value = "/delete/{commentId}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String deleteComment(@PathVariable Long commentId, 
                                @CookieValue(value = "JWT", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return "로그인이 필요합니다.";
        }

        Long userNo = userService.getUserNo(token);
        Optional<CommunityComment> optionalComment = communityCommentService.findCommentById(commentId);
        
        if (optionalComment.isPresent()) {
            CommunityComment comment = optionalComment.get();

            if (comment.getUserNo().getUserNo().equals(userNo)) {
                communityCommentService.deleteComment(commentId, userNo);
                return "SUCCESS";
            } else {
                return "댓글 작성자만 삭제할 수 있습니다.";
            }
        }

        return "댓글을 삭제할 수 없습니다.";
    }
}
