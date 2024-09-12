package kr.co.ifit.api.controller;

import kr.co.ifit.api.request.PostReq;
import kr.co.ifit.api.response.PostRes;
import kr.co.ifit.api.service.PostService;
import kr.co.ifit.db.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/new")
    public ResponseEntity<Map<String, String>> createPost(@RequestBody PostReq postReq) {
        Map<String, String> response = new HashMap<>();

        try {
            PostRes post = postService.createPost(postReq);
            response.put("status", "success");
            response.put("message", "Post created successfully");
            response.put("postId", String.valueOf(post.getPostId()));
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("message", "Failed to create post" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();

        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<PostRes> getPost(@PathVariable long id) {
        PostRes postRes = postService.getPost(id);

        return ResponseEntity.ok(postRes);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, String>> updatePost(@PathVariable long id, @RequestBody PostReq postReq) {
        Map<String, String> response = new HashMap<>();
        boolean isUpdated = postService.updatePost(id, postReq);

        if (isUpdated) {
            response.put("status", "success");
            response.put("message", "Post updated successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("status", "fail");
            response.put("message", id + "- Post not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deletePost(@PathVariable long id) {

        Map<String, String> response = new HashMap<>();
        boolean isDeleted = postService.deletePost(id);
        if (isDeleted) {
            response.put("status", "success");
            response.put("message", "Post deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("status", "fail");
            response.put("message", id + "- Post not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

}
