package kr.co.ifit.repository;

import kr.co.ifit.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByLoginId(String loginId);
}
