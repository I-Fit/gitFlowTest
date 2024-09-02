package kr.co.ifit.db.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Setter
@Getter
@Entity
@ToString
@NoArgsConstructor
@Table(name = "Community")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long communityId;

    @Column(name = "communityName", nullable = false)
    private String title;

    @Column(name = "communityDescription", nullable = false)
    private String topboxContent;

    @Column(name = "exerciseName", nullable = false)
    private String sport;

    @Column(name = "address", nullable = false)
    private String location;

    @Column(name = "peopleTotal", nullable = false)
    private Integer person;

    @Column(name = "communityDatetime", columnDefinition = "TIMESTAMP")
    private LocalDateTime date;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    public Group(String title, String topboxContent, String sport, String location, Integer person, LocalDateTime date, User user) {
        this.title = title;
        this.topboxContent = topboxContent;
        this.sport = sport;
        this.location = location;
        this.person = person;
        this.date = date;
        this.user = user;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
