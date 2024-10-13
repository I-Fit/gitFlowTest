package kr.co.ifit.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import kr.co.ifit.api.request.UserDtoReq;
import kr.co.ifit.api.response.LoginDtoRes;
import kr.co.ifit.api.response.UserDtoRes;
import kr.co.ifit.api.service.EmailVerificationService;
import kr.co.ifit.api.service.UserService;
import kr.co.ifit.common.auth.JwtTokenProvider;
import kr.co.ifit.common.util.UserContextUtil;
import kr.co.ifit.db.entity.EmailVerification;
import kr.co.ifit.db.entity.Token;
import kr.co.ifit.db.entity.User;
import kr.co.ifit.db.repository.EmailVerificationRepository;
import kr.co.ifit.db.repository.TokenRepository;
import kr.co.ifit.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserController {

    private final UserService userService;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailVerificationService emailVerificationService;
    private final EmailVerificationRepository emailVerificationRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserContextUtil userContextUtil;

    // 회원가입
    @PostMapping("/user-account")
    public ResponseEntity<String> registerUser(@RequestBody UserDtoReq userDtoReq) {
        String email = userDtoReq.getEmail();
        String username = userDtoReq.getUsername();
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("이메일은 필수입니다.");
        }
        if (username == null || username.isEmpty()) {
            return ResponseEntity.badRequest().body("이름은 필수입니다.");
        }


        Boolean isEmailVerified = emailVerificationRepository.findByUserEmail(email)
                .map(EmailVerification::getEmailVerified).orElse(false);
        if (isEmailVerified) {
            try {
                userService.registerUser(userDtoReq);
                return ResponseEntity.ok("회원가입 성공");
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            } catch (Exception e) {
                return ResponseEntity.status(500).body("회원가입 실패: " + e.getMessage());
            }
        } else {
            return ResponseEntity.ok("이메일 인증을 완료해주세요.");
        }
    }

    // 아이디 중복 확인 (회원가입 시)
    @PostMapping("/check-id")
    public ResponseEntity<?> checkId(@RequestBody UserDtoReq userDtoReq) {
        String id = userDtoReq.getLoginId();
        boolean available = userService.checkIdAvailability(id);

        if (available) {
            return ResponseEntity.ok("존재하는 아이디입니다.");
        } else {
            return ResponseEntity.ok("사용가능한 아이디입니다.");
        }
    }

    //  아이디 찾기
    @PostMapping("/find-id")
    public ResponseEntity<?> findIdUser(@RequestBody UserDtoReq userDtoReq) {
        String email = userDtoReq.getEmail();

        Boolean isEmailVerified = emailVerificationRepository.findByUserEmail(email)
                .map(EmailVerification::getEmailVerified).orElse(false);

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
        logger.info("성공========================================================================= {}", loginId);
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

        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("존재하지 않는 사용자입니다.");
        }
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 변경 중 오류가 발생했습니다.");
        }
    }

//    @PostMapping("/user/password/change")
//    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeDtoReq passwordChangeDtoReq) {
//        Long userId = userContextUtil.getAuthenticatedUserId();
//        if (userId == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//        passwordChangeDtoReq.setUserId(userId);
//
//        try {
//            passwordChangeService.changePassword(passwordChangeDtoReq);
//            return ResponseEntity.ok().body(Map.of("message", "비밀번호 변경 성공"));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(Map.of("message", "비밀번호 변경 실패: " + e.getMessage()));
//        }
//    }

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDtoReq userDtoReq) {
        String loginId = userDtoReq.getLoginId();
        String password = userDtoReq.getPassword();
        logger.info("연결 성공========================================================================= {}", loginId);

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
            // 사용자 이름 가져오기
            String userName = user.getUsername();
            // 사용자 이미지 가져오기
            String userProfile = user.getProfileUrl();

            // 응답 반환
            LoginDtoRes loginDtoRes = new LoginDtoRes("로그인 성공", accessToken, refreshToken, userId, userName, userProfile);
            return ResponseEntity.ok(loginDtoRes);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("서버 오류: " + e.getMessage());
        }
    }

    //  로그아웃 - refreshToken을 보내서 검증 후 데이터베이스에서 찾아서 삭제 후 로그아웃 성공
    @DeleteMapping("/logout")
    @Transactional
    public ResponseEntity<?> logoutUser(HttpServletRequest request) {
        String refreshToken = resolveToken(request);

        if (refreshToken != null) {
            String loginId = jwtTokenProvider.extractUsername(refreshToken);
            boolean isTokenValid = jwtTokenProvider.validateToken(refreshToken, loginId);


            if (isTokenValid) {
                Optional<Token> tokenOptional = tokenRepository.findByRefreshToken(refreshToken);

                if (tokenOptional.isPresent()) {
                    Token token = tokenOptional.get();
                    User user = token.getUser();
                    Long userId = user.getUserId();

                    logger.info("사용자Id 뭐가 뜨나========================================================================= {}", userId);

                    // 사용자 ID로 토큰을 찾기
                    tokenRepository.deleteByUser_UserId(userId);
                    return ResponseEntity.ok("로그아웃 성공");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("토큰을 찾을 수 없습니다.");
                }
            } else {
                return ResponseEntity.badRequest().body("토큰이 만료 되었습니다.");
            }
        }
        return ResponseEntity.badRequest().body("refresh token 이 제공되지 않았습니다.");
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("refresh-token");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @GetMapping("/user-info")
    public ResponseEntity<?> userInfo() {
        Long userId = userContextUtil.getAuthenticatedUserId();
        if (userId == null) {
            //  인증되지 않은 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        UserDtoRes userDtoRes = new UserDtoRes(
                userId,
                user.getUsername(),
                user.getProfileUrl()
        );
        return ResponseEntity.ok(userDtoRes);
    }

    // 회원탈퇴
    @DeleteMapping("/delete-account")
    @Transactional
    public ResponseEntity<?> deleteAccount(HttpServletRequest request) {
        String refreshToken = resolveToken(request);

        if (refreshToken != null) {
            String loginId = jwtTokenProvider.extractUsername(refreshToken);
            boolean isTokenValid = jwtTokenProvider.validateToken(refreshToken, loginId);

            logger.info("아이디 뭐가 뜨나========================================================================= {}", loginId);

            if (isTokenValid) {
                Optional<Token> tokenOptional = tokenRepository.findByRefreshToken(refreshToken);

                if (tokenOptional.isPresent()) {
                    Token token = tokenOptional.get();
                    User user = token.getUser();
                    Long userId = user.getUserId();

                    logger.info("=회원탈퇴====================================================================== {}", userId);

//                    userRepository.deleteByUserId(userId);
//                    return ResponseEntity.ok().build();
                    // 사용자 삭제 및 관련 데이터 삭제
                    boolean isDeleted = userService.deleteUserAndRelatedData(userId);

                    if (isDeleted) {
                        return ResponseEntity.ok().build();
                    } else {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(Map.of("message", "회원 탈퇴에 실패했습니다."));
                    }
                }
            }
        }
        return ResponseEntity.badRequest().body("refresh token 이 제공되지 않았습니다."); // 토큰이 제공되지 않은 경우의 응답
    }
}
