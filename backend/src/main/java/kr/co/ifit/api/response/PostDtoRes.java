package kr.co.ifit.api.response;

import kr.co.ifit.db.entity.Post;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDtoRes {

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
        this.isHeartFilled = post.getLikesCnt() > 0;    // 초기화
    }

    public PostDtoRes(Post post, boolean isHeartFilled) {
        this(post);     // 기존 생성자 호출
        this.isHeartFilled = isHeartFilled;     // 좋아요 상태 설정
    }
}
