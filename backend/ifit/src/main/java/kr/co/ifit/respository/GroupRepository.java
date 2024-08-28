package kr.co.ifit.respository;

import kr.co.ifit.domain.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// JPA 자동 구현
@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
    //  userId로 Group 리스트를 조회하는 메서드 정의
    List<Group> findByUserId(Integer userId);
}
