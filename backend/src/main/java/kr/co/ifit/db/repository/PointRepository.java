package kr.co.ifit.db.repository;

import kr.co.ifit.db.entity.Point;
import kr.co.ifit.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {
    Point findByUser(User user);
    List<Point> findByUser_UserId(Long userId);

    // 특정 사용자 ID의 총 포인트 조회
    @Query("SELECT SUM(p.amount) FROM Point p WHERE p.user.userId = :userId")
    Integer findTotalPointsByUserId(Long userId);
}
