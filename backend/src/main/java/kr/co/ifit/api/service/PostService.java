package kr.co.ifit.api.service;

import kr.co.ifit.api.request.PostDtoReq;
import kr.co.ifit.api.response.PostDtoRes;
import kr.co.ifit.db.entity.Post;
import kr.co.ifit.db.repository.CommentRepository;
import kr.co.ifit.db.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    // 게시글 생성
    public PostDtoRes createPost(PostDtoReq postReq, String imageStr) {
        // post 엔티티 생성
        Post post = new Post(
                postReq.getTitle(),
                postReq.getContent(),
                imageStr,
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
    @Transactional(readOnly = true)
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
//            post.setImageStr(postReq.getImageStr());
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
    @Transactional
    public boolean deletePost(Long postId) {
        if (postRepository.existsById(postId)) {
            commentRepository.deleteByPostId(postId);
            postRepository.deleteById(postId);
            return true;
        } else {
            return false;
        }
    }

    // 게시글 목록
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // 게시글 좋아요
    public PostDtoRes likePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        int likesCnt = post.getLikesCnt();
        boolean isHeartFilled;

        if (likesCnt > 0) {
            post.setLikesCnt(post.getLikesCnt() - 1);
            isHeartFilled = false;
        } else {
            post.setLikesCnt(post.getLikesCnt() + 1);
            isHeartFilled = true;
        }
        postRepository.save(post);

        return new PostDtoRes(post, isHeartFilled);
    }

    // 키워드로 게시글 검색
    public List<Post> searchByKeyword(String keyword) {
        return postRepository.searchByKeyword(keyword);
    }

    // 운동명으로 게시글 검색
    public List<Post> searchByExercise(String exercise) {
        List<Post> posts = postRepository.findAllByExercise(exercise);

        return posts;
    }

    // 게시글 정렬
    public List<Post> getSortedPosts(String sort, String direction) {

        // 생성일 기준으로 정렬
        Sort.Direction sortDirection = (direction != null &&
                direction.equalsIgnoreCase("DESC")) ? Sort.Direction.DESC : Sort.Direction.ASC;

        // 정렬 기준 없는 경우 기본값은 생성일 순
        if (sort == null || sort.isEmpty()) {
            sort = "createdAt";
        }

        switch (sort) {
            case "older":
                return postRepository.findAll(Sort.by(Sort.Direction.ASC, "createdAt"));
            case "morePopular":
                return postRepository.findAll(Sort.by(sortDirection, "likesCnt"));
            default:
                return postRepository.findAll(Sort.by(sortDirection, "createdAt"));
        }
    }

}
