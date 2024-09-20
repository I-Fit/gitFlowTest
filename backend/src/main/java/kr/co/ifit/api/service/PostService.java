package kr.co.ifit.api.service;

import kr.co.ifit.api.request.PostDtoReq;
import kr.co.ifit.api.response.PostDtoRes;
import kr.co.ifit.db.entity.Post;
import kr.co.ifit.db.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    // 게시글 생성
    public PostDtoRes createPost(PostDtoReq postReq) {
        // post 엔티티 생성
        Post post = new Post(
                postReq.getTitle(),
                postReq.getContent(),
                postReq.getImageUrl(),
                postReq.getExercise(),
                postReq.getLocation(),
                postReq.getUserId()
        );

        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        // post 엔티티 저장
        Post savedPost = postRepository.save(post);

        // PostRes 객체 생성 후 반환
        return new PostDtoRes(savedPost);
    }

    // 게시글 상세
    public PostDtoRes getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

//        long likeCnt = postRepository.findLikeCntById(id);
//        long commentCnt = postRepository.findCommentCntById(id);

//        postRes.setLikesCnt(likeCnt);
//        postRes.setCommentsCnt(commentCnt);

        return new PostDtoRes(post);
    }

    // 게시글 수정
    public boolean updatePost(Long id, PostDtoReq postReq) {
        if (postRepository.existsById(id)) {
            Post post = postRepository.findById(id).orElseThrow();

            post.setTitle(postReq.getTitle());
            post.setContent(postReq.getContent());
            post.setImageUrl(postReq.getImageUrl());
            post.setExercise(postReq.getExercise());
            post.setLocation(postReq.getLocation());
            post.setUpdatedAt(LocalDateTime.now());

            postRepository.save(post);
            return true;
        } else {
            return false;
        }
    }

    // 게시글 삭제
    public boolean deletePost(Long id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    // 게시글 목록
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
}
