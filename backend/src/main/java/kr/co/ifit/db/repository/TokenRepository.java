package kr.co.ifit.db.repository;

import kr.co.ifit.db.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByRefreshToken(String refreshToken);
    Token findByUser_UserId(Long userId);
    List<Token> findByExpirationBefore(LocalDateTime now);

    @Modifying
    @Transactional
    @Query("DELETE FROM Token t WHERE t.user.userId = :userId")
    void deleteByUser_UserId(@Param("userId") Long userId);
}
