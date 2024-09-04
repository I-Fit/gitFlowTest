package kr.co.ifit.db.repository;


import kr.co.ifit.db.entity.Group;
import kr.co.ifit.db.entity.JoinedGroup;
import kr.co.ifit.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JoinedGroupRepository extends JpaRepository<JoinedGroup, Long> {
    //   특정 사용자에 의해 참여한 모든 엔티티 반환
    List<JoinedGroup> findByUser(User user);

    Optional<JoinedGroup> findByUserAndGroup(User user, Group group);

    boolean existsByUserAndGroup(User user, Group group);
}
