package kr.co.ifit.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Dibs")
public class LikedGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int dibsId;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;


}
