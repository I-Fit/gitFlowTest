package kr.co.ifit.db.repository;

import kr.co.ifit.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByLoginId(String loginId);
    User findByLoginId(String loginId);
    Optional<User> findByEmail(String email);
    User findByUserName(String userName);
}