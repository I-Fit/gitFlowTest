package kr.co.ifit.db.repository;

import jakarta.transaction.Transactional;
import kr.co.ifit.db.entity.Coupon;
import kr.co.ifit.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    List<Coupon> findByUser(User user);
    List<Coupon> findByUser_UserIdAndIsUsedFalse(Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Coupon c WHERE c.user.userId = :userId")
    void deleteByUser_UserId(@Param("userId") Long userId);

}
