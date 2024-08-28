package kr.co.ifit.respository;

import kr.co.ifit.domain.entity.LikedGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikedGroupRepository extends JpaRepository<LikedGroup, Long> {
}
