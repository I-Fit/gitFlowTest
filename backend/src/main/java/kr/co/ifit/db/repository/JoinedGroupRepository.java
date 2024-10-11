package kr.co.ifit.db.repository;


import kr.co.ifit.db.entity.Group;
import kr.co.ifit.db.entity.JoinedGroup;
import kr.co.ifit.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JoinedGroupRepository extends JpaRepository<JoinedGroup, Long> {
    //   특정 사용자에 의해 참여한 모든 엔티티 반환
    List<JoinedGroup> findByUser(User user);

    Optional<JoinedGroup> findByUserAndGroup(User user, Group group);

    boolean existsByUserAndGroup(User user, Group group);

    // 정렬된 모임을 반환하는 메서드
    @Query("SELECT j FROM JoinedGroup j WHERE j.user = :user ORDER BY CASE " +
            "WHEN :sortOption = 1 THEN j.group.peopleParticipation END DESC, " + // 인기순
            "CASE WHEN :sortOption = 2 THEN j.group.date END DESC, " + // 최신순
            "CASE WHEN :sortOption = 3 THEN j.group.date END ASC") // 오래된순
    List<JoinedGroup> findJoinedGroupsSorted(@Param("user") User user, @Param("sortOption") int sortOption);

    @Query("SELECT j FROM JoinedGroup j JOIN j.group g WHERE j.user = :user AND (" +
            "LOWER(g.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(g.topboxContent) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(g.sport) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(g.location) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<JoinedGroup> findByUserAndSearchTerm(@Param("user") User user, @Param("searchTerm") String searchTerm);

}
