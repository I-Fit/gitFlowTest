package kr.co.ifit.db.repository;

import kr.co.ifit.db.entity.Group;
import kr.co.ifit.db.entity.LikedGroup;
import kr.co.ifit.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikedGroupRepository extends JpaRepository<LikedGroup, Long> {
    //  사용자Id에 따라 좋아요한 모임 목록을 반환
    List<LikedGroup> findByUser(Long userId);
    LikedGroup findByUserAndGroup(User user, Group group);
}
