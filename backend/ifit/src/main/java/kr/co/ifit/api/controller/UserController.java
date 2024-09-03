package kr.co.ifit.api.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.ifit.db.entity.User;
import kr.co.ifit.api.request.LoginDTO;
import kr.co.ifit.api.request.UserDTO;
import kr.co.ifit.api.service.UserService;
import kr.co.ifit.api.service.EmailService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;

    @Autowired
    public UserController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    // 회원가입
    @PostMapping("/user-account")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
        try {
            userService.registerUser(userDTO);
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

    // 이메일 인증
    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("email") String email, @RequestParam("code") String code) {
        try {
            String result = emailService.verifyEmail(email, code);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("이메일 인증 실패: " + e.getMessage());
        }
    }

    // 로그인
    @PostMapping("/user-login")
    public ResponseEntity<String> loginUser(@RequestBody LoginDTO loginDTO, HttpSession session) {
        try {
            boolean authenticated = userService.loginUser(loginDTO.getLoginId(), loginDTO.getPassword(), session);
            if (authenticated) {
                User user = (User) session.getAttribute("user");
                if (user != null) {
                    return ResponseEntity.ok("로그인 성공. 사용자: " + user.getUserName());
                } else {
                    return ResponseEntity.status(500).body("로그인 성공 후 사용자 정보를 세션에서 찾을 수 없습니다.");
                }
            } else {
                return ResponseEntity.status(401).body("로그인 실패: 아이디 또는 비밀번호가 틀렸습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("로그인 실패: " + e.getMessage());
        }
    }

    // 로그아웃
    @PostMapping("/user-logout")
    public ResponseEntity<String> logoutUser(HttpSession session) {
        try {
            userService.logoutUser(session); // Service에서 세션 무효화
            return ResponseEntity.ok("로그아웃 성공");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("로그아웃 실패: " + e.getMessage());
        }
    }

    @Setter
    @Getter
    public static class IdCheckResponse {
        private boolean available;

        public IdCheckResponse(boolean available) {
            this.available = available;
        }
    }
}