package kr.co.ifit.db.repository;

import kr.co.ifit.db.entity.Group;
import kr.co.ifit.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// JPA 자동 구현
@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    //  userId로 Group 리스트를 조회하는 메서드 정의
    List<Group> findByUser(User user);
}
