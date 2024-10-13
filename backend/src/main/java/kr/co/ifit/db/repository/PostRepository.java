package kr.co.ifit.db.repository;

import jakarta.transaction.Transactional;
import kr.co.ifit.api.response.PostDtoRes;
import kr.co.ifit.db.entity.Post;
import kr.co.ifit.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

//    Long findLikeCntById(@Param("postId") Long postId);
//    Long findCommentCntById(@Param("postId") Long postId);

    List<Post> findByUser(User user);

    List<Post> findByUser_UserId(Long userId);
    List<Post> findAllByExercise(String exercise);

    @Query("Select p from Post p where p.exercise like %:keyword% or p.title like %:keyword% or p.content like %:keyword%")
    List<Post> searchByKeyword(@Param("keyword") String keyword);

    List<Post> findAllByUser(User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM Post p WHERE p.user.userId = :userId")
    void deleteByUser_UserId(@Param("userId") Long userId);

}
