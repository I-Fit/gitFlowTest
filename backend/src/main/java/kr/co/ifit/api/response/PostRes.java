package kr.co.ifit.api.response;

import kr.co.ifit.db.entity.Post;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostRes {

    private Long postId;
    private String title;
    private String content;
    private String imageUrl;
    private String author;  // User user
    private String exercise;
    private String location;
    private LocalDateTime createdAt;
    private long likesCnt;
    private long commentsCnt;

    public PostRes(Post post) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.author = post.getAuthor();
        this.exercise = post.getExercise();
        this.location = post.getLocation();
        this.createdAt = post.getCreatedAt();
        this.imageUrl = post.getImageUrl();
    }
}
