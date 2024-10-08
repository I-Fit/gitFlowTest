package kr.co.ifit.api.controller;

import kr.co.ifit.api.request.PostDtoReq;
import kr.co.ifit.api.response.PostDtoRes;
import kr.co.ifit.api.service.PostService;
import kr.co.ifit.common.util.UserContextUtil;
import kr.co.ifit.db.entity.Post;
import kr.co.ifit.db.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserContextUtil userContextUtil;

    // 게시글 생성
    @PostMapping("/new")
    public ResponseEntity<Map<String, String>> createPost(@ModelAttribute PostDtoReq postReq,
                                                          @RequestPart("imageStr") MultipartFile image
                                                          ) {

        Long userId = userContextUtil.getAuthenticatedUserId();
        if (userId == null) {
            //  인증되지 않은 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Map<String, String> response = new HashMap<>();
        System.out.println("image file: " + image.getOriginalFilename());

        try {
            System.out.println("createPost() 호출 성공");
            String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
//            Long userId = extractUserIdFromToken(token);
//            postReq.setImageStr(base64Image);

            PostDtoRes post = postService.createPost(postReq, base64Image, userId);

            response.put("status", "success");
            response.put("message", "Post created successfully");
            response.put("postId", String.valueOf(post.getPostId()));

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("message", "Failed to create post" + e.getMessage());
            e.printStackTrace();

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // 모든 게시글 가져오기
    @GetMapping("/list")
    public ResponseEntity<List<Post>> getAllPosts() {
        System.out.println("Fetching all posts");
        List<Post> posts = postService.getAllPosts();

        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

//    @GetMapping("/list")
//    public List<Post> getAllPosts() {
//        System.out.println("Fetching all posts");
//        List<Post> posts = postService.getAllPosts();
//
//        return posts;
//    }

    // 특정 게시글 가져오기
    @GetMapping("/post/{id}")
    public ResponseEntity<PostDtoRes> getPost(@PathVariable Long id) {
        PostDtoRes postRes = postService.getPost(id);

        return ResponseEntity.ok(postRes);
    }

    // 게시글 수정
    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, String>> updatePost(@PathVariable Long id,
                                                          @RequestBody PostDtoReq postReq
                                                          ) {
        Long userId = userContextUtil.getAuthenticatedUserId();
        if (userId == null) {
            //  인증되지 않은 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Map<String, String> response = new HashMap<>();

        boolean isUpdated = postService.updatePost(id, postReq, userId);

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
        Long userId = userContextUtil.getAuthenticatedUserId();
        if (userId == null) {
            //  인증되지 않은 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Map<String, String> response = new HashMap<>();

        boolean isDeleted = postService.deletePost(postId, userId);
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
        PostDtoRes response = postService.likePost(id);
        return ResponseEntity.ok(response);
    }

    // 키워드로 게시글 검색
    @GetMapping("/search")
    public ResponseEntity<List<Post>> searchByKeyword(@RequestParam String keyword) {
        List<Post> posts = postService.searchByKeyword(keyword);

        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    // 운동명으로 게시글 검색
//    @GetMapping("/search")
//    public ResponseEntity<List<Post>> searchByExercise(@RequestParam String exercise) {
//        List<Post> posts = postService.searchByExercise(exercise);
//
//        return new ResponseEntity<>(posts, HttpStatus.OK);
//    }

    // 게시글 정렬
    @GetMapping("/sort")
    public ResponseEntity<List<Post>> getSortedPosts(@RequestParam(required = false) String sort,
                                                     @RequestParam(required = false) String direction) {
        List<Post> posts = postService.getSortedPosts(sort, direction);

        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    private Long extractUserIdFromToken(String token) {
        return 3L;
    }
}
