package kr.co.ifit.api.controller;

import kr.co.ifit.api.request.CommentDtoReq;
import kr.co.ifit.api.response.CommentDtoRes;
import kr.co.ifit.api.service.CommentService;
import kr.co.ifit.common.util.UserContextUtil;
import kr.co.ifit.db.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final UserContextUtil userContextUtil;

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    // 댓글 생성
    @PostMapping("/new")
    public ResponseEntity<Map<String, String>> createComment(@RequestBody CommentDtoReq commentReq) {
        Long userId = userContextUtil.getAuthenticatedUserId();
        if (userId == null) {
            //  인증되지 않은 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        commentReq.setUserId(userId);
        Map<String, String> response = new HashMap<>();
        logger.info("성공========================================================================= {}", commentReq.getPostId());
        logger.info("성공========================================================================= {}", commentReq.getContent());

        try {
            CommentDtoRes comment = commentService.createComment(commentReq);

            response.put("status", "success");
            response.put("message", "Comment created successfully");
            response.put("commentId", String.valueOf(comment.getCommentId()));

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println("error creating comment: " + e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to create comment: " + e.getMessage());

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // 특정 댓글 가져오기
    @GetMapping("/{id}")
    public ResponseEntity<CommentDtoRes> getComment(@PathVariable Long id) {
        CommentDtoRes commentRes = commentService.getComment(id);

        return new ResponseEntity<>(commentRes, HttpStatus.OK);
    }

    // 모든 댓글 리스트 가져오기
    @GetMapping("/list")
    public ResponseEntity<List<CommentDtoRes>> getAllComments() {
        List<CommentDtoRes> comments = commentService.getAllComments();

        return ResponseEntity.ok(comments);
    }

    // 특정 게시글의 댓글 가져오기
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDtoRes>> getCommentsByPostId(@PathVariable Long postId) {

        System.out.println("받은 postid: " + postId);
        Long userId = userContextUtil.getAuthenticatedUserId();

        logger.info("========================================================================= {}", postId);
        List<CommentDtoRes> comments = commentService.getCommentsByPostId(postId);
        System.out.println("fetched comments for postId: " + postId);

        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    // 특정 유저가 작성한 댓글 가져오기
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<Comment>> getCommentsByUserId(@PathVariable Long userId) {
//
//        List<Comment> comments = commentService.getCommentsByUserId(userId);
//
//        return ResponseEntity.ok(comments);
//    }

    // 내가 쓴 댓글
    @GetMapping("/by")
    public ResponseEntity<List<CommentDtoRes>> getCommentsByUserId() {
        Long userId = userContextUtil.getAuthenticatedUserId();
        if (userId == null) {
            //  인증되지 않은 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<CommentDtoRes> comments = commentService.getCommentsByUserId(userId);

        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    // 댓글 수정
    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, String>> updateComment(@PathVariable Long id, @RequestBody CommentDtoReq commentReq) {
        Map<String, String> response = new HashMap<>();
        boolean isUpdated = commentService.updateComment(id, commentReq);

        if (isUpdated) {
            response.put("status", "success");
            response.put("message", "Comment updated successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("status", "fail");
            response.put("message", id + ": Failed to update comment");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    // 댓글 삭제
    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<Map<String, String>> deleteComment(@PathVariable Long commentId) {
        Long userId = userContextUtil.getAuthenticatedUserId();
        if (userId == null) {
            //  인증되지 않은 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Map<String, String> response = new HashMap<>();

        if (commentId == null || commentId <= 0) {
            response.put("status", "fail");
            response.put("message", "Invalid comment Id");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        boolean isDeleted = commentService.deleteComment(commentId);
        if (isDeleted) {
            response.put("status", "success");
            response.put("message", "Comment deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("status", "fail");
            response.put("message", commentId + ": Failed to delete comment");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
