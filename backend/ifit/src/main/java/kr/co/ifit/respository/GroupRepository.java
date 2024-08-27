package kr.co.ifit.respository;

import kr.co.ifit.domain.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// JPA 자동 구현
@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {

}
