package kr.co.ifit.db.repository;

import kr.co.ifit.db.entity.Payment;
import kr.co.ifit.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByUser(User user);

    @Query("SELECT p FROM Payment p WHERE p.user.userId = :userId ORDER BY p.date DESC")
    Optional<Payment> findLatestByUserId(@Param("userId") Long userId);
}
