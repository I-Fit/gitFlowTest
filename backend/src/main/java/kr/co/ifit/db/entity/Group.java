package kr.co.ifit.db.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


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
    private int person;

    @Column(name = "peopleParticipation", nullable = false)
    private int peopleParticipation = 0;

    @Column(name = "communityDatetime", columnDefinition = "TIMESTAMP")
    private LocalDateTime date;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JoinedGroup> joinedGroups = new ArrayList<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikedGroup> likedGroups = new ArrayList<>();

    public Group(String title, String topboxContent, String sport, String location, int person, LocalDateTime date, User user) {
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

    public void incrementPeopleParticipation() {
        if (peopleParticipation <= person) {
            this.peopleParticipation++;
        }
    }
    public void decrementPeopleParticipation() {
        if (peopleParticipation > 0) {
            this.peopleParticipation--;
        }
    }
}
