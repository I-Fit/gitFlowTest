package kr.co.ifit.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
//    @JsonIgnore
    private Long communityId;

    @Column(name = "name", nullable = false)
    private String title;

    @Column(name = "`desc`", nullable = false)
    private String topboxContent;

    @Column(name = "exercise", nullable = false)
    private String sport;

    @Column(name = "location", nullable = false)
    private String fullLocation;

    @Column(name = "address", nullable = false)
    private String location;

    @Column(name = "total_ppl", nullable = false)
    private int person;

    @Column(name = "party_ppl", nullable = false)
    private int peopleParticipation = 0;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime date;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_saved", nullable = false)
    private boolean saved = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore     // 생성자가 생기지 않도록 하기 위해 작성
    private User user;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JoinedGroup> joinedGroups = new ArrayList<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikedGroup> likedGroups = new ArrayList<>();

    public Group(String title, String topboxContent, String sport, String fullLocation, String location, int person,LocalDateTime date, User user, boolean saved) {
        this.title = title;
        this.topboxContent = topboxContent;
        this.sport = sport;
        this.fullLocation = fullLocation;
        this.location = location;
        this.person = person;
        this.date = date;
        this.user = user;
        this.saved = saved;
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
