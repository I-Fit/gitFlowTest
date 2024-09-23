package kr.co.ifit.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.ifit.api.request.UserDtoReq;
import kr.co.ifit.api.response.LoginDtoRes;
import kr.co.ifit.api.service.EmailVerificationService;
import kr.co.ifit.api.service.UserService;
import kr.co.ifit.common.auth.JwtTokenProvider;
import kr.co.ifit.db.entity.EmailVerification;
import kr.co.ifit.db.entity.Token;
import kr.co.ifit.db.entity.User;
import kr.co.ifit.db.repository.EmailVerificationRepository;
import kr.co.ifit.db.repository.TokenRepository;
import kr.co.ifit.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailVerificationService emailVerificationService;
    private final EmailVerificationRepository emailVerificationRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // 회원가입
    @PostMapping("/user-account")
    public ResponseEntity<String> registerUser(@RequestBody UserDtoReq userDtoReq) {
        try {
            userService.registerUser(userDtoReq);
            return ResponseEntity.ok("회원가입 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("회원가입 실패: " + e.getMessage());
        }
    }
    // 아이디 중복 확인 (회원가입 시)
    @GetMapping("/check-id")
    public ResponseEntity<?> checkId(@RequestBody UserDtoReq userDtoReq) {
        String id = userDtoReq.getLoginId();
        boolean available = userService.checkIdAvailability(id);

        if (available) {
            return ResponseEntity.ok("존재하는 아이디입니다.");
        } else {
            return ResponseEntity.ok("사용 가능한 아이디입니다.");
        }
    }

    //  아이디 찾기
    @PostMapping("/find-id")
    public ResponseEntity<?> findIdUser(@RequestBody UserDtoReq userDtoReq) {
        String email = userDtoReq.getEmail();
        String enteredCode = userDtoReq.getEnteredCode();

        boolean isEmailVerified = emailVerificationService.verifyEmail(email, enteredCode);
        if (isEmailVerified) {
            String loginId = userService.findUserIdByEmail(email);
            if (loginId != null) {
                return ResponseEntity.ok(loginId);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "사용자를 찾을 수 없습니다."));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","이메일 인증 실패"));
        }
    }

    //  비밀번호 재설정 - 아이디 입력 부분
    @PostMapping("/password/check-id")
    public ResponseEntity<?> checkIdPassword(@RequestBody UserDtoReq userDtoReq) {
        String loginId = userDtoReq.getLoginId();
        boolean available = userService.checkIdAvailability(loginId);
        if (available) {
            return ResponseEntity.ok("확인");
        } else {
            return ResponseEntity.ok("존재하지 않는 아이디입니다.");
        }
    }

    // 비밀번호 재설정 - 이메일 인증
    @PostMapping("/password/emailVerification")
    public ResponseEntity<?> findPassword(@RequestBody UserDtoReq userDtoReq) {
        String loginId = userDtoReq.getLoginId();
        String email = userDtoReq.getEmail();
        String enteredCode = userDtoReq.getEnteredCode();
        //  아이디로 사용자 정보 찾기
        User user = userService.findByLoginId(loginId);
        //  사용자 정보에서 이메일과 입력된 이메일이 일치하는지 확인
        if (!user.getEmail().equals(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("가입한 이메일과 일치 하지 않습니다.");
        }

        boolean isEmailVerified = emailVerificationService.verifyEmail(email, enteredCode);
        if (isEmailVerified) {
            return ResponseEntity.ok("이메일 인증을 성공했습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "이메일 인증 실패 또는 인증 번호가 다릅니다."));
        }
    }

    //  비밀번호 재설정
    @PostMapping("/password/modified")
    public ResponseEntity<?> modifyPassword(@RequestBody UserDtoReq userDtoReq) {
        String password = userDtoReq.getPassword();
        String passwordCheck = userDtoReq.getPasswordCheck();
        String loginId = userDtoReq.getLoginId();
        String email = userDtoReq.getEmail();

        if (!password.equals(passwordCheck)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호가 일치하지 않습니다.");
        }

        User user = userService.findByLoginId(loginId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("존재하지 않는 사용자입니다.");
        }

        Optional<EmailVerification> emailVerification = emailVerificationRepository.findByUserEmail(email);
        if (emailVerification.isEmpty() || !emailVerification.get().getEmailVerified()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일 인증이 필요합니다.");
        }

        // 기존 비밀번호와 일치하는지 확인
        String existingEncodedPassword = user.getPassword();
        if (passwordEncoder.matches(password, existingEncodedPassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("새 비밀번호와 기존 비밀번호가 일치합니다.");
        }

        try {
            userService.updatePassword(loginId, password);
            return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 변경 중 오류가 발생했습니다.");
        }
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDtoReq userDtoReq) {
        String loginId = userDtoReq.getLoginId();
        String password = userDtoReq.getPassword();

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
            LocalDateTime expirationTime = ZonedDateTime.now().plusMinutes(jwtTokenProvider.getRefreshExpiration() / 1000 / 60).toLocalDateTime();

            Token refreshTokenEntity = new Token();
            refreshTokenEntity.setRefreshToken(refreshToken);
            refreshTokenEntity.setExpiration(expirationTime);
//            refreshTokenEntity.setCreatedAt(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime());
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

    //  로그아웃 - refreshToken을 보내서 검증 후 데이터베이스에서 찾아서 삭제 후 로그아웃 성공
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request) {
        String refreshToken = resolveToken(request);

        if (refreshToken != null) {
            String loginId = jwtTokenProvider.extractUsername(refreshToken);
            boolean isTokenValid = jwtTokenProvider.validateToken(refreshToken, loginId);

            if (isTokenValid) {
                Optional<Token> tokenOptional = tokenRepository.findByRefreshToken(refreshToken);
                if (tokenOptional.isPresent()) {
                    //  유효한 토큰이 존재하면 삭제
                    tokenRepository.delete(tokenOptional.get());
                    return ResponseEntity.ok("로그아웃 성공");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("이미 로그아웃 처리되었습니다.");
                }
            } else {
                return ResponseEntity.badRequest().body("토큰이 만료 되었습니다.");
            }
        }
        return ResponseEntity.badRequest().body("refresh token 이 제공되지 않았습니다. ");
    }
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("refresh-token");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}