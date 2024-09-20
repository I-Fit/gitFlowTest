package kr.co.ifit.db.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
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

    @Column(name = "image_url")
    private String imageUrl;

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

//    @Transient
    private int likesCnt;

//    @Transient
    private int commentsCnt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Post(String title, String content, String imageUrl, String exercise, String location, Long userId) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.exercise = exercise;
        this.location = location;
        this.userId = userId;
    }

//    private int capacity;   // 총 모집인원
//    @Column(name = "party_cnt")
//    private int partyCnt;    // 참여인원
}
