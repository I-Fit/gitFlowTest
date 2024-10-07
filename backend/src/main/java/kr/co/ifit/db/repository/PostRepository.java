package kr.co.ifit.db.repository;

import kr.co.ifit.db.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

//    Long findLikeCntById(@Param("postId") Long postId);
//    Long findCommentCntById(@Param("postId") Long postId);
    List<Post> findAllByExercise(String exercise);

    @Query("Select p from Post p where p.exercise like %:keyword% or p.title like %:keyword% or p.content like %:keyword%")
    List<Post> searchByKeyword(@Param("keyword") String keyword);
}
