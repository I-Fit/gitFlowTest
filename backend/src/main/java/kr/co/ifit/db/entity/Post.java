package kr.co.ifit.db.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
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

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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

    @Column(name = "comments_cnt")
    private int commentsCnt;

    @Column(name = "is_heart_filled")
    private boolean isHeartFilled = false;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    public Post(String title, String content, String imageStr, String exercise, String location, User user) {
        this.title = title;
        this.content = content;
        this.imageStr = imageStr;
        this.exercise = exercise;
        this.location = location;
        this.user = user;
    }

//    public void setIsHeartFilled(boolean isHeartFilled) {
//        this.isHeartFilled = isHeartFilled;
//    }

//    public void setIsHeartFilled(boolean isHeartFilled) {
//        this.isHeartFilled = isHeartFilled;
//    }

//    private int capacity;   // 총 모집인원
//    @Column(name = "party_cnt")
//    private int partyCnt;    // 참여인원
}
