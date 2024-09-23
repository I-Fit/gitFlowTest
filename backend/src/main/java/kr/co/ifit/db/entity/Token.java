package kr.co.ifit.db.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
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
    private Long token_Id;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    @Column(name = "expired_at", nullable = false)
    @JsonFormat(timezone = "Asia/Seoul")
    private LocalDateTime expiration;          // 만료 시간

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
