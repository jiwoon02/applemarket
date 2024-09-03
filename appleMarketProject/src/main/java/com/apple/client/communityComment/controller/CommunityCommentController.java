package com.apple.client.communityComment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apple.client.communityComment.domain.CommunityComment;
import com.apple.client.communityComment.service.CommunityCommentService;

@RestController
@RequestMapping("/comments")
public class CommunityCommentController {

    @Autowired
    private CommunityCommentService communityCommentService;

    /***********************************************************
     * 댓글 리스트 구현하기
     * @return List<CommunityComment>
     * @PathVariable: URL 경로에서 데이터를 추출하는 용도로 사용
     * @produces: 응답 문서 타입을 지정 (JSON 형식)
     * 현재 요청 URL : http://localhost:8080/comments/all/{postId}
     ***********************************************************/
    @GetMapping(value = "/all/{postId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CommunityComment> getCommentList(@PathVariable Long postId) {
        return communityCommentService.findCommentsByPostId(postId);
    }

    /***********************************************************
     * 댓글 등록하기
     * @return String
     * @RequestBody: JSON 데이터를 원하는 타입으로 바인딩 처리
     * @consumes: 클라이언트 요청 시 데이터 타입 지정 (JSON 형식)
     * @produces: 응답 시 데이터 타입 지정 (문자열 형식)
     * 현재 요청 URL : http://localhost:8080/comments/insert
     ***********************************************************/
    @PostMapping(value = "/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String insertComment(@RequestBody CommunityComment comment) {
        CommunityComment createdComment = communityCommentService.createComment(comment);
        return (createdComment != null) ? "SUCCESS" : "FAILURE";
    }

    /***********************************************************
     * 댓글 수정하기
     * @return String
     * 현재 요청 URL : http://localhost:8080/comments/update/{commentId}
     ***********************************************************/
    @PutMapping(value = "/update/{commentId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String updateComment(@PathVariable Long commentId, @RequestBody CommunityComment commentDetails) {
        CommunityComment updatedComment = communityCommentService.updateComment(commentId, commentDetails);
        return (updatedComment != null) ? "SUCCESS" : "FAILURE";
    }

    /***********************************************************
     * 댓글 삭제하기
     * @return String
     * 현재 요청 URL : http://localhost:8080/comments/delete/{commentId}
     ***********************************************************/
    @DeleteMapping(value = "/delete/{commentId}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String deleteComment(@PathVariable Long commentId) {
        communityCommentService.deleteComment(commentId);
        return "SUCCESS";
    }
}
