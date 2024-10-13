package kr.co.ifit.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "emailverification")
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long emailverificationId;

    @Column(name = "auth_code", nullable = false, length = 50)
    private String emailCode;//  생성된 인증 코드

    @Column(name = "user_email", nullable = false, length = 50)
    private String userEmail;

    @Column(name = "is_verified", nullable = true)
    private Boolean emailVerified = false;

    @Column(name = "expired_at", nullable = true)
    private LocalDateTime expiryTime;

    @OneToOne(optional = false)          //  User 엔티티와 1대1 관계 설정
    @JoinColumn(name = "user_id", nullable = true)
    private User user;
}
