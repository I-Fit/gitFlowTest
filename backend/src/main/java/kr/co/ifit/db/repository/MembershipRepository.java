package kr.co.ifit.db.repository;

import kr.co.ifit.db.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    Optional<Membership> findByGrade(String grade);
}
