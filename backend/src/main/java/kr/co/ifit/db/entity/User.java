package kr.co.ifit.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "user")
@ToString(exclude = "token")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 사용자 ID 자동 생성
    @Column(name = "id", nullable = false)
//    @JsonIgnore
    private Long userId;

    @Column(name = "login_id", nullable = false, length = 50)
    private String loginId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "phone", nullable = false, length = 11)
    private String phoneNumber;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "created_at", nullable = true)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = true)
    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    @Lob
    @Column(name = "profile_url", nullable = true)
    private String profileUrl;

    @ElementCollection
    @CollectionTable(
            name = "user_liked_posts", // 생성할 테이블 이름
            joinColumns = @JoinColumn(name = "user_id") // 이 테이블의 user_id 외래 키
    )
    @Column(name = "liked_post_id") // liked_post_id 컬럼 이름
    private List<Long> likedPosts = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private EmailVerification emailVerification;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Token token;

    // 매개변수를 받는 생성자
    public User(String loginId, String password, String username, String phoneNumber, String email) {
        this.loginId = loginId;
        this.password = password;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }
}