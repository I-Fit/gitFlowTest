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
public class CommentRes {

    private Long commentId;
    private Long postId;
    private String content;
    private String author;
    private LocalDateTime createdAt;

    public CommentRes(Comment comment) {
        this.commentId = comment.getCommentId();
        this.postId = comment.getPostId();
        this.content = comment.getContent();
        this.author = comment.getAuthor();
//        this.createdAt = comment.getCreatedAt();
    }
}
