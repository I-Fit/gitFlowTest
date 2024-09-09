package kr.co.ifit.db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "users")
@ToString
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 사용자 ID 자동 생성
    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "loginId", nullable = false, length = 50)
    private String loginId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "userName", nullable = false, length = 50)
    private String userName;

    @Column(name = "phoneNumber", nullable = false, length = 11)
    private String phoneNumber;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "createdAt", nullable = true)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "modifiedAt", nullable = true)
    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    @Lob
    @Column(name = "profileUrl", nullable = true)
    private String profileUrl;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "emailverificationId")
    private EmailVerification emailVerification;

    // 매개변수를 받는 생성자
    public User(String loginId, String password, String userName, String phoneNumber, String email) {
        this.loginId = loginId;
        this.password = password;
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }
}