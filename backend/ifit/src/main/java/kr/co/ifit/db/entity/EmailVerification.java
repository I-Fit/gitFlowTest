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
    private Long emailverificationId;

    @Column(name = "emailCode", nullable = false, length = 50)
    private String emailCode;//  생성된 인증 코드

    @Column(name = "user_email", nullable = false, length = 50)
    private String userEmail;

    @Column(name = "emailVerified", nullable = true)
    private Boolean emailVerified = false;

    @Column(name = "expiryTime", nullable = true)
    private LocalDateTime expiryTime;

    @OneToOne(optional = true)          //  User 엔티티와 1대1 관계 설정
    @JoinColumn(name = "userId", nullable = true)
    private User user;
}
