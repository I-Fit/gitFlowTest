package kr.co.ifit.db.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "coupon")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "percentage")
    private Double percentage;

    @Column(name = "created_at", nullable = false)
    @JsonFormat(timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @Column(name = "expired_at", nullable = false)
    @JsonFormat(timezone = "Asia/Seoul")
    private LocalDateTime expiredAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "is_used", nullable = false)
    private Boolean isUsed = false;

}