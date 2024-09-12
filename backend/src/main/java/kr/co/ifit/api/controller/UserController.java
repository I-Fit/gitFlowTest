package kr.co.ifit.api.controller;

import kr.co.ifit.api.response.LoginDtoRes;
import kr.co.ifit.common.auth.JwtTokenProvider;
import kr.co.ifit.common.util.JwtUtil;
import kr.co.ifit.db.entity.Token;
import kr.co.ifit.api.request.LoginDtoReq;
import kr.co.ifit.api.request.UserDtoReq;
import kr.co.ifit.api.service.UserService;
import kr.co.ifit.db.entity.User;
import kr.co.ifit.db.repository.TokenRepository;
import kr.co.ifit.db.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;



    // 회원가입
    @PostMapping("/user-account")
    public ResponseEntity<String> registerUser(@RequestBody UserDtoReq userDtoReq) {
        try {
            userService.registerUser(userDtoReq);
            return ResponseEntity.ok("회원가입 성공. 이메일을 확인하여 인증해주세요.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("회원가입 실패: " + e.getMessage());
        }
    }

    // 아이디 중복 확인
    @GetMapping("/check-id")
    public ResponseEntity<IdCheckResponse> checkId(@RequestParam("id") String id) {
        boolean available = userService.checkIdAvailability(id);
        return ResponseEntity.ok(new IdCheckResponse(available));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDtoReq loginDtoReq) {
        String loginId = loginDtoReq.getLoginId();
        String password = loginDtoReq.getPassword();

        try {
            if (!userService.authenticateUser(loginId, password)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("로그인 실패: 잘못된 사용자 ID 또는 비밀번호입니다.");
            }

            // JWT 토큰 생성
            String accessToken = jwtTokenProvider.generateToken(loginId);
            String refreshToken = jwtTokenProvider.generateRefreshToken(loginId);

            User user = userRepository.findByLoginId(loginId);
            // Refresh Token을 DB에 저장
            Instant now = Instant.now();
            Instant expiration = now.plusSeconds(jwtTokenProvider.getRefreshExpiration() / 1000);

            Token refreshTokenEntity = new Token();
            refreshTokenEntity.setRefreshToken(refreshToken);
            refreshTokenEntity.setExpiration(LocalDateTime.ofInstant(expiration, ZoneId.systemDefault()));
            refreshTokenEntity.setUser(user);

            tokenRepository.save(refreshTokenEntity);

            // 사용자 식별 ID 가져오기
            Long userId = user.getUserId();

            // 응답 반환
            LoginDtoRes loginDtoRes = new LoginDtoRes("로그인 성공", accessToken, refreshToken, userId);
            return ResponseEntity.ok(loginDtoRes);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("서버 오류: " + e.getMessage());
        }
    }

//    @PostMapping("/login")
//    public ResponseEntity<String> loginUser(@RequestBody LoginDTO loginDTO) {
//        try {
//            String userName = loginDTO.getUserName();
////            String loginId = loginDTO.getLoginId();
//            String password = loginDTO.getPassword();
//
//            boolean isAuthenticated = userService.authenticateUser(userName, password);
//            if (isAuthenticated) {
//                return ResponseEntity.ok("로그인 성공");
//            } else {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패: 잘못된 사용자 Id 또는 비밀번호 입니다.");
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류: " + e.getMessage());
//        }
//    }
//
//
//    // 로그아웃
//    @PostMapping("/user-logout")
//    public ResponseEntity<String> logoutUser(HttpSession session) {
//        try {
//            userService.logoutUser(session); // Service에서 세션 무효화
//            return ResponseEntity.ok("로그아웃 성공");
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("로그아웃 실패: " + e.getMessage());
//        }
//    }

    @Setter
    @Getter
    public static class IdCheckResponse {
        private boolean available;

        public IdCheckResponse(boolean available) {
            this.available = available;
        }
    }
}