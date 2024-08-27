package kr.co.ifit.respository;


import kr.co.ifit.domain.entity.JoinGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JoinGroupRepository extends JpaRepository<JoinGroup, Integer> {
}
