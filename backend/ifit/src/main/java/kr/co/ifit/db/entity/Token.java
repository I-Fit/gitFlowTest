package kr.co.ifit.db.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "token")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)     //  token 테이블 PK
    private Long token_Id;

    @Column(name = "refreshToken", nullable = false)
    private String refreshToken;

    @Column(name = "expiration", nullable = false)
    private LocalDateTime expiration;           // 만료 시간

    @Column(name = "createdAt", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    // 생성 시간
    @Column(name = "updatedAt", nullable = true)
    @UpdateTimestamp
    private LocalDateTime updatedAt;            // 갱신 시간

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    public void updateToken(String newRefreshToken, LocalDateTime newExpiration) {
        this.refreshToken = newRefreshToken;
        this.expiration = newExpiration;
    }
}
