package kr.co.ifit.api.controller;

import kr.co.ifit.api.request.CommentDtoReq;
import kr.co.ifit.api.response.CommentDtoRes;
import kr.co.ifit.api.service.CommentService;
import kr.co.ifit.db.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/new")
    public ResponseEntity<Map<String, String>> createComment(@RequestBody CommentDtoReq commentReq) {
        Map<String, String> response = new HashMap<>();

        try {
            CommentDtoRes comment = commentService.createComment(commentReq);

            response.put("status", "success");
            response.put("message", "Comment created successfully");
            response.put("commentId", String.valueOf(comment.getCommentId()));

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to create comment" + e.getMessage());

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // 특정 댓글 가져오기
    @GetMapping("/get/{id}")
    public ResponseEntity<CommentDtoRes> getComment(@PathVariable Long id) {
        CommentDtoRes commentRes = commentService.getComment(id);

        return new ResponseEntity<>(commentRes, HttpStatus.OK);
    }

    // 모든 댓글 리스트 가져오기
//    @GetMapping("/list")
//    public ResponseEntity<List<Comment>> getAllComments() {
//        List<Comment> comments = commentService.getAllComments();
//
//        return new ResponseEntity<>(comments, HttpStatus.OK);
//    }

    // 특정 게시글의 댓글 가져오기
    @GetMapping("/get/post/{postId}")
    public ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable Long postId) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);

        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    // 특정 유저가 작성한 댓글 가져오기
    @GetMapping("/get/user/{userId}")
    public ResponseEntity<List<Comment>> getCommentsByUserId(@PathVariable Long userId) {
        List<Comment> comments = commentService.getCommentsByUserId(userId);

        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteComment(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        boolean isDeleted = commentService.deleteComment(id);
        if (isDeleted) {
            response.put("status", "success");
            response.put("message", "Comment deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("status", "fail");
            response.put("message", id + ": Failed to delete comment");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
