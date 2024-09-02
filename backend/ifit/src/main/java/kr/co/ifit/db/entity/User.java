package kr.co.ifit.db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "Users")
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

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modifiedAt", nullable = false)
    private LocalDateTime modifiedAt;

    @Lob
    @Column(name = "profileUrl")
    private String profileUrl;

    @Column(name = "emailVerified", nullable = false)
    private Boolean emailVerified = false;

    @Column(name = "emailCode")
    private String emailCode;

    @Column(name = "emailcodeExpiry")
    private LocalDateTime emailcodeExpiry;

    // 기본 생성자
    public User() {}

    // 매개변수를 받는 생성자
    public User(String loginId, String password, String userName, String phoneNumber, String email) {
        this.loginId = loginId;
        this.password = password;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        modifiedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", loginId='" + loginId + '\'' +
                ", password='" + password + '\'' +
                ", userName='" + userName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                ", profileUrl='" + profileUrl + '\'' +
                ", emailVerified=" + emailVerified +
                ", emailCode='" + emailCode + '\'' +
                ", emailcodeExpiry=" + emailcodeExpiry +
                '}';
    }
}