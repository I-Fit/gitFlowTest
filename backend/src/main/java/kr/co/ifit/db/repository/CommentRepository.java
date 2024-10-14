package kr.co.ifit.db.repository;

import jakarta.transaction.Transactional;
import kr.co.ifit.api.response.CommentDtoRes;
import kr.co.ifit.db.entity.Comment;
import kr.co.ifit.db.entity.Post;
import kr.co.ifit.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByUser(User user);
    List<Comment> findAllByPost(Post post);

    List<Comment> findByUser_UserId(Long userId);
    List<Comment> findByPost_PostId(Long postId);

    List<CommentDtoRes> findByContentContaining(String content);

    void deleteByPost(Post post);

    @Modifying
    @Transactional
    @Query("DELETE FROM Comment c WHERE c.user.userId = :userId")
    void deleteByUser_UserId(@Param("userId") Long userId);

}