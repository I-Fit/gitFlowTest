package kr.co.ifit.db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

// 사용자가 모임에 참가한 정보를 저장하는 테이블을 나타낸다
@Setter
@Getter
@Entity
@ToString
@NoArgsConstructor          // 기본 생성자를 자동으로 만들어주는 어노테이션
@Table(name = "`Join`")   // 데이터베이스 테이블과 매핑된다
public class JoinedGroup {
    @Id                     // 기본 키를 나타낸다
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long communityJoinId;

    @Column(name = "joinedAt", nullable = false)
    private LocalDateTime joinedAt;

    // 다대일 관계를 정의 : 하나의 join은 하나의 user와 하나의 group에 속한다?
    // 외래키 정의
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "communityId", nullable = false)
    private Group group;

}
