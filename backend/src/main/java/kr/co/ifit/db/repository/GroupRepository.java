package kr.co.ifit.db.repository;

import kr.co.ifit.api.response.GroupDtoRes;
import kr.co.ifit.db.entity.Group;
import kr.co.ifit.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

// JPA 자동 구현
@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    //  userId로 Group 리스트를 조회하는 메서드 정의
    List<Group> findByUser(User user);

    //  모든 exercise 값을 가져오는 메서드 정의
    @Query("SELECT DISTINCT g.sport FROM Group g WHERE g.sport IS NOT NULL")
    List<String> findAllExercises();

    // 운동 종목을 포함하는 모임 리스트 반환
    @Query("SELECT g FROM Group g WHERE g.sport = :sport")
    List<Group> findBySport(@Param("sport") String sport);

    // 지역을 포함하는 모임 리스트 반환
    @Query("SELECT g FROM Group g WHERE g.location = :location")
    List<Group> findByLocation(@Param("location") String location);

    // 날짜로 필터링
    @Query("SELECT g FROM Group g WHERE g.date >= :startOfDay AND g.date < :nextDate")
    List<Group> findGroupsByDate(@Param("startOfDay")LocalDateTime startOfDay, @Param("nextDate")LocalDateTime nextDate);

    //  시간으로 필터링
//    @Query("SELECT g FROM Group g WHERE TIME(g.date) = :time")
//    List<Group> findGroupsByTime(@Param("time") LocalTime time);
    @Query("SELECT g FROM Group g WHERE DATE_FORMAT(g.date, '%H') = :hour AND DATE_FORMAT(g.date, '%i') =:minute")
    List<Group> findGroupsByTime(@Param("hour") int hour, @Param("minute") int minute);

    //  검색어가 포함된 모임을 찾는 메서드
    @Query("SELECT g FROM Group g WHERE " +
            "LOWER(g.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(g.topboxContent) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(g.sport) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(g.location) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Group> findByAnyFieldContaining(@Param("searchTerm") String searchTerm);

    @Query("SELECT g FROM Group g WHERE g.user.userId = :userId AND (" +
            "LOWER(g.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(g.topboxContent) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(g.sport) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(g.location) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Group> findByUserIdAndAnyFieldContaining(@Param("userId") Long userId, @Param("searchTerm") String searchTerm);

//    List<Group> findByTitleContaining(String searchTerm);

    // 캐러셀 이미지 ID와 관련된 모임을 조회
//    @Query("SELECT g FROM Group g WHERE g.imageId = :imageId ORDER BY g.date")
//    List<Group> findByImageIdOrderByDate(@Param("imageId") Long imageId);
//    List<Group> findByImageId(Long imageId);

    // 정렬순에 관련한 메서드 - homeGroup
    @Query("SELECT g FROM Group g ORDER BY CASE " +
            "WHEN :sortOption = 1 THEN g.peopleParticipation END DESC, " + // 인기순
            "CASE WHEN :sortOption = 2 THEN g.date END DESC, " + // 최신순
            "CASE WHEN :sortOption = 3 THEN g.date END ASC") // 오래된순
    List<Group> findAllSorted(@Param("sortOption") int sortOption);

    //  사용자가 만든 모임을 먼저 검색 후 다음 정렬순에 관련한 모임 리스트 - createdGroup
    @Query("SELECT g FROM Group g WHERE g.user.userId = :userId ORDER BY CASE " +
            "WHEN :sortOption = 1 THEN g.peopleParticipation END DESC, " + // 인기순
            "CASE WHEN :sortOption = 2 THEN g.date END DESC, " + // 최신순
            "CASE WHEN :sortOption = 3 THEN g.date END ASC") // 오래된순
    List<Group> findByIdSorted(@Param("userId") Long userId, @Param("sortOption") int sortOption);
}
