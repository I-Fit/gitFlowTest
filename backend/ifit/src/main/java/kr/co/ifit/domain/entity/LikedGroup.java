package kr.co.ifit.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
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

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "communityId", nullable = false)
    private Group group;

    public LikedGroup() {
    }

//    public LikedGroup(User user, Group group) {
//        this.user = user;
//        this.group = group;
//    }

    @Override
    public String toString() {
        return "LikedGroup{" +
                "dibsId=" + dibsId +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", user=" + user +
                ", group=" + group +
                '}';
    }
}
