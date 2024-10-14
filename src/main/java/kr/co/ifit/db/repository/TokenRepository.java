package kr.co.ifit.db.repository;

import kr.co.ifit.db.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByRefreshToken(String refreshToken);
    List<Token> findByUser_UserId(Long userId);
    List<Token> findByExpirationBefore(LocalDateTime now);
}
