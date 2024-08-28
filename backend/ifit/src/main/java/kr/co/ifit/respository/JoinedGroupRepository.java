package kr.co.ifit.respository;


import kr.co.ifit.domain.entity.JoinedGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JoinedGroupRepository extends JpaRepository<JoinedGroup, Integer> {
}
