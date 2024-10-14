package kr.co.ifit.common.model;

import io.jsonwebtoken.JwtException;
import kr.co.ifit.common.auth.JwtTokenProvider;
import kr.co.ifit.db.entity.Token;
import kr.co.ifit.db.entity.User;
import kr.co.ifit.db.repository.TokenRepository;
import kr.co.ifit.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RefreshTokenCleanupTask {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    //  1시간 마다 실행
    //  3600000
    @Scheduled(fixedRate = 604800000)
    public void cleanupExpiredTokens() {
        //  현재 시간을 가져옴
//        LocalDateTime now = LocalDateTime.now();
        ZonedDateTime now = ZonedDateTime.now();
        //   현재 시간보다 만료된 토큰을 데이터베이스에서 조회
        List<Token> expiredTokens = tokenRepository.findByExpirationBefore(now.toLocalDateTime());
        //  만료된 토큰 목록을 순회하며 각각에 대해 처리
        for (Token token : expiredTokens) {
            try {
                //  만료된 토큰에서 사용자 로그인 Id를 추출
                String loginId = jwtTokenProvider.extractUsername(token.getRefreshToken());
                // loginId로 사용자 식별 Id 조회
                User user = userRepository.findByLoginId(loginId);
                //  사용자가 존재하는 경우, 사용자 Id를 가져와 로그아웃 처리
                if (user != null) {
                    Long userId = user.getUserId();
                    invalidateUserToken(userId);
                } else {
                    // 사용자를 찾을 수 없는 경우 처리
                    System.out.println("사용자를 찾을 수 없습니다." + loginId);
                }
            } catch (JwtException e) {
                System.out.println("만료된 토큰입니다." + token.getRefreshToken());
            } finally {
                tokenRepository.delete(token);
            }
        }
    }
    private void invalidateUserToken (Long userId){
        //  사용자 Id에 해당하는 모든 토큰을 찾는다
        List<Token> userTokens = tokenRepository.findByUser_UserId(userId);
        //  토큰 삭제
        tokenRepository.deleteAll(userTokens);
        System.out.println("인증 만료, 재로그인 해주세요.");
    }
}
