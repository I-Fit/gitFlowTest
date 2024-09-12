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
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    private String content;

    private String author;  //User user
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    public Comment(String content, String author, LocalDateTime createdAt, Long postId) {

        this.content = content;
        this.author = author;
        this.createdAt = createdAt;
        this.postId = postId;
    }

    // 수정일
//    @Column(name = "updated_at", columnDefinition = "TIMESTAMP")
//    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Seoul")
//    private LocalDateTime updatedAt;
}
