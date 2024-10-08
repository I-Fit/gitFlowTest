package kr.co.ifit.db.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "board")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long postId;

    private String title;

    private String content;

    @Column(name = "image_str")
    private String imageStr;

    private String exercise;

    private String location;

    @Column(name = "user_id")
    private Long userId;  // User user

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

    @Column(name = "likes_cnt")
    private int likesCnt;

    @Setter
    @Getter
    @Column(name = "is_heart_filled")
    private boolean isHeartFilled;

    @Column(name = "comments_cnt")
    private int commentsCnt;

    public Post(String title, String content, String imageStr, String exercise, String location, Long userId) {
        this.title = title;
        this.content = content;
        this.imageStr = imageStr;
        this.exercise = exercise;
        this.location = location;
        this.userId = 1L;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Post(String title, String content, String imageStr, String exercise, String location, Long userId, int likesCnt, boolean isHeartFilled) {
        this.title = title;
        this.content = content;
        this.imageStr = imageStr;
        this.exercise = exercise;
        this.location = location;
        this.userId = 1L;
        this.likesCnt = 0;
        this.isHeartFilled = false;
    }

    public void setIsHeartFilled(boolean isHeartFilled) {
        this.isHeartFilled = isHeartFilled;
    }

//    public boolean isHeartFilled() {
//        return isHeartFilled;
//    }

//    private int capacity;   // 총 모집인원
//    @Column(name = "party_cnt")
//    private int partyCnt;    // 참여인원
}
