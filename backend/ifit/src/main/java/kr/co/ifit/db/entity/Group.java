package kr.co.ifit.db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Setter
@Getter
@Entity
@Table(name = "Community")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer communityId;

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

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    //    기본 생성자
    public Group() {

    }

    //    파라미터가 있는 생성자
    public Group(String title, String topboxContent, String sport, String location, Integer person, LocalDateTime date, User user) {
        this.title = title;
        this.topboxContent = topboxContent;
        this.sport = sport;
        this.location = location;
        this.person = person;
        this.date = date;
        this.user = user;
    }

    @Override
    public String toString() {
        return "AddGroup{" +
                "communityId=" + communityId +
                ", title='" + title + '\'' +
                ", topboxContent='" + topboxContent + '\'' +
                ", sport='" + sport + '\'' +
                ", location='" + location + '\'' +
                ", person=" + person +
                ", date='" + date +
                ", createAt='" + createdAt +
                ", userId='" + user +
                '}';
    }
}
