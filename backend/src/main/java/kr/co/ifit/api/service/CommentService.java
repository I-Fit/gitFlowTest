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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 댓글 생성
    public CommentDtoRes createComment(CommentDtoReq commentReq) {

        // 게시글 조회
        Post post = postRepository.findById(commentReq.getPost().getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // 댓글 생성
        Comment comment = new Comment(
                commentReq.getContent(),
                commentReq.getUser(),
                commentReq.getPost()
        );
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
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    // 특정 게시글의 댓글 가져오기
    public List<Comment> getCommentsByPostId(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return commentRepository.findAllByPost(post);
    }

    // 특정 유저가 작성한 댓글 가져오기
    public List<Comment> getCommentsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return commentRepository.findAllByUser(user);
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
