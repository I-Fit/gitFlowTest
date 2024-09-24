package kr.co.ifit.api.controller;

import kr.co.ifit.api.request.PostDtoReq;
import kr.co.ifit.api.response.PostDtoRes;
import kr.co.ifit.api.service.PostService;
import kr.co.ifit.db.entity.Post;
import kr.co.ifit.db.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;

    // 게시글 생성
    @PostMapping("/new")
    public ResponseEntity<Map<String, String>> createPost(@RequestBody PostDtoReq postReq) {
        Map<String, String> response = new HashMap<>();

        try {
            PostDtoRes post = postService.createPost(postReq);
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

    // 모든 게시글 가져오기
    @GetMapping("/list")
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();

        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    // 특정 게시글 가져오기
    @GetMapping("/post/{id}")
    public ResponseEntity<PostDtoRes> getPost(@PathVariable Long id) {
        PostDtoRes postRes = postService.getPost(id);

        return ResponseEntity.ok(postRes);
    }

    // 게시글 수정
    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, String>> updatePost(@PathVariable Long id, @RequestBody PostDtoReq postReq) {
        Map<String, String> response = new HashMap<>();
        boolean isUpdated = postService.updatePost(id, postReq);

        if (isUpdated) {
            response.put("status", "success");
            response.put("message", "Post updated successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("status", "fail");
            response.put("message", id + ": Post not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    // 게시글 삭제
    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<Map<String, String>> deletePost(@PathVariable Long postId) {

        Map<String, String> response = new HashMap<>();
        boolean isDeleted = postService.deletePost(postId);
        if (isDeleted) {
            response.put("status", "success");
            response.put("message", "Post deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("status", "fail");
            response.put("message", postId + ": Post not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    // 게시글 좋아요
    @PostMapping("/{id}/like")
    public ResponseEntity<PostDtoRes> likePost(@PathVariable Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setLikesCnt(post.getLikesCnt() + 1);
        postRepository.save(post);

        return ResponseEntity.ok(new PostDtoRes(post));
    }

    // 운동명으로 게시글 검색
    @GetMapping("/search")
    public ResponseEntity<List<Post>> searchByExercise(@RequestParam String exercise) {
        List<Post> posts = postService.searchByExercise(exercise);

        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    // 게시글 정렬
    @GetMapping("/sort")
    public ResponseEntity<List<Post>> getSortedPosts(@RequestParam(required = false) String sort,
                                                     @RequestParam(required = false) String direction) {
        List<Post> posts = postService.getSortedPosts(sort, direction);

        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
}
