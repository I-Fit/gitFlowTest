package kr.co.ifit.db.repository;

import kr.co.ifit.db.entity.EmailVerification;
import kr.co.ifit.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
    Optional<EmailVerification> findByUser(User user);
    Optional<EmailVerification> findByUserEmail(String userEmail);
    void deleteByUserEmail(String userEmail);
}
