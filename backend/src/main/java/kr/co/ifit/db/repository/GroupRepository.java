package kr.co.ifit.db.repository;

import kr.co.ifit.api.response.GroupDtoRes;
import kr.co.ifit.db.entity.Group;
import kr.co.ifit.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// JPA 자동 구현
@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    //  userId로 Group 리스트를 조회하는 메서드 정의
    List<Group> findByUser(User user);

    //  모든 exercise 값을 가져오는 메서드 정의
    @Query("SELECT DISTINCT g.sport FROM Group g WHERE g.sport IS NOT NULL")
    List<String> findAllExercises();

//    List<Group> findAll();

    //  검색어가 포함된 모임을 찾는 메서드
    @Query("SELECT g FROM Group g WHERE " +
            "LOWER(g.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(g.topboxContent) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(g.sport) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(g.location) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Group> findByAnyFieldContaining(@Param("searchTerm") String searchTerm);

//    List<Group> findByTitleContaining(String searchTerm);

    // 캐러셀 이미지 ID와 관련된 모임을 조회
//    @Query("SELECT g FROM Group g WHERE g.imageId = :imageId ORDER BY g.date")
//    List<Group> findByImageIdOrderByDate(@Param("imageId") Long imageId);
//    List<Group> findByImageId(Long imageId);

    // 정렬순에 관련한 메서드
    @Query("SELECT g FROM Group g ORDER BY CASE " +
            "WHEN :sortOption = 1 THEN g.peopleParticipation END DESC, " + // 인기순
            "CASE WHEN :sortOption = 2 THEN g.date END DESC, " + // 최신순
            "CASE WHEN :sortOption = 3 THEN g.date END ASC") // 오래된순
    List<Group> findAllSorted(@Param("sortOption") int sortOption);
}
