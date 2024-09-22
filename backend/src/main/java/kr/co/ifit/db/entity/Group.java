package kr.co.ifit.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.PostConstruct;
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
@AllArgsConstructor
@Table(name = "`group`")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long communityId;

    @Column(name = "name", nullable = false)
    private String title;

    @Column(name = "`desc`", nullable = false)
    private String topboxContent;

    @Column(name = "exercise", nullable = false)
    private String sport;

    @Column(name = "address", nullable = false)
    private String location;

    @Column(name = "total_ppl", nullable = false)
    private int person;

    @Column(name = "party_ppl", nullable = false)
    private int peopleParticipation = 0;

    @Column(name = "date_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime date;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore     // 생성자가 생기지 않도록 하기 위해 작성
    private User user;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JoinedGroup> joinedGroups = new ArrayList<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikedGroup> likedGroups = new ArrayList<>();

    public Group(String title, String topboxContent, String sport, String location, int person,LocalDateTime date, User user) {
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
