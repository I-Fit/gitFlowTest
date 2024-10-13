package kr.co.ifit.db.repository;

import jakarta.transaction.Transactional;
import kr.co.ifit.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByLoginId(String loginId);
    User findByLoginId(String loginId);
    User findByEmail(String email);

    @Modifying
    @Query("DELETE FROM User u WHERE u.userId = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}