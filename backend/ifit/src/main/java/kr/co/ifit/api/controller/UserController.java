package kr.co.ifit.api.controller;

import kr.co.ifit.api.response.LoginDtoRes;
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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;


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
            boolean isAuthenticated = userService.authenticateUser(loginId, password);
            if (isAuthenticated) {
                String accessToken = jwtUtil.generateToken(loginId);  //  jwt 토큰 생성 - Token
                String refreshToken = jwtUtil.generateRefreshToken(loginId);    //  refresh Token 생성

                // refresh token DB에 저장
                Token refresh_token_entity = new Token();
                refresh_token_entity.setRefreshToken(refreshToken);
                refresh_token_entity.setExpiration(LocalDateTime.now().plusSeconds(jwtUtil.getRefreshExpiration()));
                refresh_token_entity.setUser(userRepository.findByLoginId(loginId));

                tokenRepository.save(refresh_token_entity);

                // 사용자 식별 Id 가져오기
                User user = userRepository.findByLoginId(loginId);
                Long userId = user.getUserId();

                LoginDtoRes loginDtoRes =  new LoginDtoRes("로그인 성공", accessToken, userId);
                return ResponseEntity.ok(loginDtoRes);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("로그인 실패 : 잘못된 사용자 Id 또는 비밀번호 입니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("서버 오류 : " + e.getMessage());
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