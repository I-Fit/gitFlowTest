package kr.co.ifit.db.repository;

import kr.co.ifit.db.entity.Comment;
import kr.co.ifit.db.entity.Post;
import kr.co.ifit.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByUser(User user);
    List<Comment> findAllByPost(Post post);

    List<Comment> findByUser_UserId(Long userId);
    List<Comment> findByPost_PostId(Long postId);

//    @Modifying
//    void deleteByPost(Post post);
}
