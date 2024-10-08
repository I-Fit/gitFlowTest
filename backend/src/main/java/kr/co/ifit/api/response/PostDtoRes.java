package kr.co.ifit.api.response;

import kr.co.ifit.db.entity.Post;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDtoRes {

//    private Post post;
    private Long postId;
    private String title;
    private String content;
    private String imageStr;
    private Long userId;  // User user
    private String exercise;
    private String location;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int likesCnt;
    private int commentsCnt;
    private boolean isHeartFilled;

    public PostDtoRes(Post post) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.imageStr = post.getImageStr();
        this.userId = post.getUserId();
        this.exercise = post.getExercise();
        this.location = post.getLocation();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.likesCnt = post.getLikesCnt();
        this.commentsCnt = post.getCommentsCnt();
        this.isHeartFilled = post.isHeartFilled();
    }

    public PostDtoRes(int likesCnt, boolean isHeartFilled) {
        this.likesCnt = likesCnt;
        this.isHeartFilled = isHeartFilled;     // 좋아요 상태 설정
    }

}
