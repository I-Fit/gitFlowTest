package kr.co.ifit.db.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.Instant;
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
    @Column(name = "id")
    private Long id;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiration;           // 만료 시간

    @OneToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void updateToken(String newRefreshToken, LocalDateTime newExpiration) {
        this.refreshToken = newRefreshToken;
        this.expiration = newExpiration;
    }
}
