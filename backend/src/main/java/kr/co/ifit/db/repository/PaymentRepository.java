package kr.co.ifit.db.repository;

import jakarta.transaction.Transactional;
import kr.co.ifit.db.entity.Payment;
import kr.co.ifit.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByUser(User user);

    @Query("SELECT p FROM Payment p WHERE p.user.userId = :userId ORDER BY p.date DESC")
    Optional<Payment> findLatestByUserId(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Payment p WHERE p.user.userId = :userId")
    void deleteByUser_UserId(@Param("userId") Long userId);

}
