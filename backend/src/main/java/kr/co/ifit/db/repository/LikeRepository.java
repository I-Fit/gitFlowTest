package kr.co.ifit.db.repository;

import kr.co.ifit.db.entity.Like;
import kr.co.ifit.db.entity.Post;
import kr.co.ifit.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    Like findByUserAndPost(User user, Post post);
    boolean existsByUserAndPost(User user, Post post);
    void deleteByUserAndPost(User user, Post post);

    void deleteByPost(Post post);
    List<Like> findByUser_UserId(Long userId);
}
