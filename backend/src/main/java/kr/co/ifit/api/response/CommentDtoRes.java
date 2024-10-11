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

    public CommentDtoRes(Comment comment) {
        this.commentId = comment.getCommentId();
        this.postId = comment.getPost().getPostId();
        this.content = comment.getContent();
        this.userId = comment.getUser().getUserId();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }

    public CommentDtoRes(Long commentId, String content) {
        this.commentId = commentId;
        this.content = content;
    }
}
