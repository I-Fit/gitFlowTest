package kr.co.ifit.db.repository;

import jakarta.transaction.Transactional;
import kr.co.ifit.db.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Transaction t WHERE t.point.user.userId = :userId")
    void deleteByUser_UserId(@Param("userId") Long userId);

}
