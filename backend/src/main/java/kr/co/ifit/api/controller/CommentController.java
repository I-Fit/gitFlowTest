package kr.co.ifit.api.controller;

import kr.co.ifit.api.request.CommentReq;
import kr.co.ifit.api.response.CommentRes;
import kr.co.ifit.api.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/new")
    public ResponseEntity<Map<String, String>> createComment(@RequestBody CommentReq commentReq) {
        Map<String, String> response = new HashMap<>();

        try {
            CommentRes comment = commentService.createComment(commentReq);

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

    public void deleteComment() {

    }

    public void getAllComments() {

    }


}
