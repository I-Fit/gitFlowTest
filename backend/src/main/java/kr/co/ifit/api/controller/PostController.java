package kr.co.ifit.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.ifit.api.request.LikeDtoReq;
import kr.co.ifit.api.request.PostDtoReq;
import kr.co.ifit.api.response.PostDtoRes;
import kr.co.ifit.api.service.PostService;
import kr.co.ifit.common.util.UserContextUtil;
import kr.co.ifit.db.entity.Post;
import kr.co.ifit.db.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);
    private final PostRepository postRepository;

//    public PostController(PostService postService) {
//        this.postService = postService;
//    }

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
        postReq.setUserId(userId);

        Map<String, String> response = new HashMap<>();
        System.out.println("image file: " + image.getOriginalFilename());

        try {
            System.out.println("createPost() 호출 성공");
            String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
//            Long userId = extractUserIdFromToken(token);
//            postReq.setImageStr(base64Image);
            logger.info("성공========================================================================= {}", postReq.getTitle());
            logger.info("성공========================================================================= {}", postReq.getLocation());
            PostDtoRes post = postService.createPost(postReq, base64Image);

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
    //정상코드
//    @GetMapping("/list")
//    public ResponseEntity<List<PostDtoRes>> getAllPosts() {
//        System.out.println("Fetching all posts");
//        List<PostDtoRes> posts = postService.getAllPosts();
//
////        if (posts.isEmpty()) {
////            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
////        }
//        return ResponseEntity.ok(posts);
//    }
    // 전체 게시글 목록
    @GetMapping("/list")
    public ResponseEntity<List<PostDtoRes>> getAllPosts() {
        System.out.println("Fetching all posts");
        List<PostDtoRes> posts = postService.getAllPosts(); // 수정된 서비스 메서드 호출
        return ResponseEntity.ok(posts);
    }


    // 특정 게시글 가져오기
    @GetMapping("/post/{postId}")
    public ResponseEntity<PostDtoRes> getPost(@PathVariable Long postId) {
        Long userId = userContextUtil.getAuthenticatedUserId();

        PostDtoRes post = postService.getPost(postId, userId);
        return ResponseEntity.ok(post);
    }

    // 게시글 수정
    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, String>> updatePost(@PathVariable Long id,
                                                          @ModelAttribute PostDtoReq postReq,
                                                          @RequestPart(value = "imageStr", required = false) MultipartFile image) throws IOException  // 이거 추가
    {
        Long userId = userContextUtil.getAuthenticatedUserId();
        if (userId == null) {
            //  인증되지 않은 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        postReq.setUserId(userId);

        Map<String, String> response = new HashMap<>();

        Post existing = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));

        String base64Image;
        if (image != null && !image.isEmpty()) {
            base64Image = Base64.getEncoder().encodeToString(image.getBytes());
        } else {
            base64Image = existing.getImageStr();
        }

        boolean isUpdated = postService.updatePost(id, postReq, base64Image);

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
    @DeleteMapping("/delete")
    public ResponseEntity<String> deletePost(@RequestParam Long postId) {
        Long userId = userContextUtil.getAuthenticatedUserId();
        if (userId == null) {
            //  인증되지 않은 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean isDeleted = postService.deletePost(postId, userId);
        if (isDeleted) {
            return ResponseEntity.ok("Post deleted successfully");
        } else {
            return ResponseEntity.status(400).body("Post not found");
        }
    }

    // 게시글 좋아요
//    @PostMapping("/{id}/like")
//    public ResponseEntity<PostDtoRes> likePost(@PathVariable Long id,
//                                               @RequestBody PostDtoReq postReq) {
//        postReq.setPostId(id);
//        PostDtoRes response = postService.likePost(postReq);
//        return ResponseEntity.ok(response);
//    }
    @RequestMapping(value = "/like", method = {RequestMethod.POST, RequestMethod.DELETE})
    public ResponseEntity<String> toggleLike(@RequestBody LikeDtoReq likeReq, HttpServletRequest request) {
        try {
            Long userId = userContextUtil.getAuthenticatedUserId();
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            likeReq.setUserId(userId);

            if (request.getMethod().equalsIgnoreCase("POST")) {
                postService.likePost(likeReq);
                return ResponseEntity.ok("좋아요 추가");
            } else if (request.getMethod().equalsIgnoreCase("DELETE")) {
                postService.unlikePost(likeReq);
                return ResponseEntity.ok("좋아요 취소");
            } else {
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

//    @PostMapping("/{id}/like")
//    public ResponseEntity<PostDtoRes> likePost(@PathVariable Long id,
//                                               @AuthenticationPrincipal UserPrincipal currentUser) {
//        PostDtoRes response = postService.likePost(id, currentUser);
//        return ResponseEntity.ok(response);
//    }

//    @DeleteMapping("/{id}/like")
//    public ResponseEntity<PostDtoRes> unlikePost(@PathVariable Long id, @RequestBody PostDtoReq postReq) {
//        postReq.setPostId(id);
//        PostDtoRes response = postService.unlikePost(postReq);
//        return ResponseEntity.ok(response);
//    }

    // 키워드로 게시글 검색
    @GetMapping("/search")
    public ResponseEntity<List<PostDtoRes>> searchByKeyword(@RequestParam String keyword) {
        List<PostDtoRes> posts = postService.searchByKeyword(keyword);

        return ResponseEntity.ok(posts);
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
    public ResponseEntity<List<PostDtoRes>> getSortedPosts(@RequestParam(required = false) String sort,
                                                           @RequestParam(required = false) String direction) {
        List<PostDtoRes> posts = postService.getSortedPosts(sort, direction);
        System.out.println("received sort: " + sort + " direction: " + direction);
        return ResponseEntity.ok(posts);
    }

    // 게시글 관리
    // 내가 쓴 게시글
    @GetMapping("/posts/by")
    public ResponseEntity<List<Post>> getPostsByUserId() {
        Long userId = userContextUtil.getAuthenticatedUserId();
        System.out.println("Authenticated UserId: " + userId);
        if (userId == null) {
            //  인증되지 않은 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Post> posts = postService.getPostsByUserId(userId);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    // 내가 좋아요한 게시글
    @GetMapping("/posts/liked")
    public ResponseEntity<List<PostDtoRes>> getLikedPostsByUserId() {
        Long userId = userContextUtil.getAuthenticatedUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<PostDtoRes> likedPosts = postService.getLikedPostsByUserId(userId);
        return new ResponseEntity<>(likedPosts, HttpStatus.OK);
    }
}
