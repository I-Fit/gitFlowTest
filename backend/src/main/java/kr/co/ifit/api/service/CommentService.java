package kr.co.ifit.api.service;

import kr.co.ifit.api.request.CommentDtoReq;
import kr.co.ifit.api.response.CommentDtoRes;
import kr.co.ifit.db.entity.Comment;
import kr.co.ifit.db.entity.Post;
import kr.co.ifit.db.entity.User;
import kr.co.ifit.db.repository.CommentRepository;
import kr.co.ifit.db.repository.PostRepository;
import kr.co.ifit.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 댓글 생성
    public CommentDtoRes createComment(CommentDtoReq commentReq) {

        // 게시글 조회
        Post post = postRepository.findById(commentReq.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User user = userRepository.findById(commentReq.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

        // 댓글 생성
//        Comment comment = new Comment(
//                commentReq.getContent(),
//                commentReq.getUserId(),
//                commentReq.getPostId(),
//        );
        Comment comment = new Comment();

        comment.setContent(commentReq.getContent());
        comment.setPost(post);
        comment.setUser(user);

        comment.setCreatedAt(LocalDateTime.now());

        commentRepository.save(comment);

        // 댓글 수 증가
        post.setCommentsCnt(post.getCommentsCnt() + 1);
        postRepository.save(post);

        return new CommentDtoRes(comment);
    }

    // 특정 댓글 가져오기
    public CommentDtoRes getComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        return new CommentDtoRes(comment);
    }

    // 모든 댓글 가져오기
    public List<CommentDtoRes> getAllComments() {
        List<Comment> comments = commentRepository.findAll();
        return comments.stream().map(CommentDtoRes::new).toList();
    }

    // 특정 게시글의 댓글 가져오기
    public List<CommentDtoRes> getCommentsByPostId(Long postId) {
        System.out.println("fetching comments for postid: " + postId);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        List<Comment> comments = commentRepository.findAllByPost(post);
        System.out.println("Comments size: " + comments.size());

        return comments.stream()
                .map(comment -> new CommentDtoRes(
                        comment.getCommentId(),
                        post.getPostId(), // 게시물 ID 추가
                        comment.getContent(),
                        comment.getUser().getUserId(), // 사용자 ID 추가
                        comment.getCreatedAt(),
                        comment.getUpdatedAt()))
                .toList();
    }

    // 특정 유저가 작성한 댓글 가져오기
    public List<CommentDtoRes> getCommentsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Comment> comments = commentRepository.findAllByUser(user);

        return comments.stream().map(comment -> new CommentDtoRes(comment.getCommentId(), comment.getContent()))
                .collect(Collectors.toList());
    }

    // 댓글 수정
    public boolean updateComment(Long id, CommentDtoReq commentReq) {
        if(commentRepository.existsById(id)) {
            Comment comment = commentRepository.findById(id).orElseThrow();

            comment.setContent(commentReq.getContent());
            comment.setUpdatedAt(LocalDateTime.now());

            commentRepository.save(comment);
            return true;
        } else {
            return false;
        }
    }

    // 댓글 삭제
    public boolean deleteComment(Long commentId) {
        if (commentRepository.existsById(commentId)) {
            Comment comment = commentRepository.findById(commentId).orElse(null);
            if (comment != null) {
                Long postId = comment.getPost().getPostId();
                commentRepository.deleteById(commentId);
                updateCommentsCount(postId);
                return true;
            }
        }
        return false;
    }

    private void updateCommentsCount(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post != null) {
            post.setCommentsCnt(post.getCommentsCnt() - 1);
            postRepository.save(post);
        }
    }
}

