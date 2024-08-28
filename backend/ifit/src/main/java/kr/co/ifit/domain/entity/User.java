package kr.co.ifit.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private int userId;

    @Column(name = "loginId")
    private String loginId;

    @Column(name = "password")
    private String password;

    @Column(name = "userName")
    private String userName;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "email", unique = true) //이메일은 유일해야하므로 unique속성 추가.
    private String email;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "modifiedAt")
    private LocalDateTime modifiedAt;

    @Column(name = "emailVerified")     // 이메일 인증 여부 필드
    private boolean isEmailVerified = false;

    @Column(name = "emailverificationCode")  // 인증 코드
    private String emailVerificationCode;

    @Column(name = "emailverificationcodeExpiry") // 인증 코드 만료일
    private LocalDateTime emailVerificationCodeExpiry;

    public User() {

    }

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
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }


    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", loginId=" + loginId +
                ", password=" + password +
                ", userName='" + userName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                '}';
    }
}