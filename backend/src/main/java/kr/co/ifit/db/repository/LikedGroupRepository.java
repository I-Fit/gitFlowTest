package kr.co.ifit.db.repository;

import jakarta.transaction.Transactional;
import kr.co.ifit.db.entity.Group;
import kr.co.ifit.db.entity.LikedGroup;
import kr.co.ifit.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LikedGroupRepository extends JpaRepository<LikedGroup, Long> {
    //  사용자Id에 따라 좋아요한 모임 목록을 반환
    List<LikedGroup> findByUser(User user);
    LikedGroup findByUserAndGroup(User user, Group group);
    boolean existsByUserAndGroup(User user, Group group);

    @Modifying
    @Transactional
    @Query("DELETE FROM LikedGroup lg WHERE lg.user.userId = :userId")
    void deleteByUser_UserId(@Param("userId") Long userId);


    // 정렬된 모임을 반환하는 메서드
//    @Query("SELECT l FROM LikedGroup l WHERE l.user = :user ORDER BY CASE " +
//            "WHEN :sortOption = 1 THEN l.group.peopleParticipation END DESC, " + // 인기순
//            "CASE WHEN :sortOption = 2 THEN l.group.date END DESC, " + // 최신순
//            "CASE WHEN :sortOption = 3 THEN l.group.date END ASC") // 오래된순
//    List<LikedGroup> findLikedGroupsSorted(@Param("user") User user, @Param("sortOption") int sortOption);
    @Query("SELECT l FROM LikedGroup l WHERE l.user = :user " +
            "ORDER BY " +
            "CASE " +
            "WHEN :sortOption = 1 THEN l.group.peopleParticipation END DESC, " +
            "CASE WHEN :sortOption = 2 THEN l.group.date END DESC, " +
            "CASE WHEN :sortOption = 3 THEN l.group.date END ASC, " +
            "l.group.date ASC") // 기본 정렬 기준 추가
    List<LikedGroup> findLikedGroupsSorted(@Param("user") User user, @Param("sortOption") int sortOption);



    @Query("SELECT lg FROM LikedGroup lg WHERE lg.user = :user AND lg.group.title LIKE %:searchTerm%")
    List<LikedGroup> findLikedGroupsByUserAndSearchTerm(@Param("user") User user, @Param("searchTerm") String searchTerm);
}
