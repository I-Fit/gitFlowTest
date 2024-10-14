package kr.co.ifit.api.response;

import kr.co.ifit.db.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDtoRes {

    private Long commentId;
    private Long postId;
    private String content;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String username;
    private String postTitle;

    public CommentDtoRes(Comment comment) {
        this.commentId = comment.getCommentId();
        this.postId = comment.getPost().getPostId();
        this.content = comment.getContent();
        this.userId = comment.getUser().getUserId();
        this.username = comment.getUser().getUsername();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }

    public CommentDtoRes(Long commentId, String content, Long postId, String postTitle, LocalDateTime createdAt) {
        this.commentId = commentId;
        this.content = content;
        this.postId = postId;
        this.postTitle = postTitle;
        this.createdAt = createdAt;
    }

    public CommentDtoRes(Long commentId, Long postId, String content, Long userId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.commentId = commentId;
        this.postId = postId;
        this.content = content;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
