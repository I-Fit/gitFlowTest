package kr.co.ifit.controller;

import kr.co.ifit.domain.dto.LoginDTO;
import kr.co.ifit.domain.dto.UserDTO;
import kr.co.ifit.service.EmailVerificationService;
import kr.co.ifit.service.UserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final EmailVerificationService emailVerificationService;

    @Autowired
    public UserController(UserService userService, EmailVerificationService emailVerificationService) {
        this.userService = userService;
        this.emailVerificationService = emailVerificationService;
    }
    // 회원가입 하는 부분
    @PostMapping("/user-account")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
        try {
            userService.registerUser(userDTO);
            return ResponseEntity.ok("회원가입 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
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
        String result = emailVerificationService.verifyEmail(email, code);
        return ResponseEntity.ok(result);
    }

    @Setter
    @Getter
    public static class IdCheckResponse {
        private boolean available;

        public IdCheckResponse(boolean available) {
            this.available = available;
        }
    }

    //로그인하는 부분
    @PostMapping("/user-login")
    public ResponseEntity<String> loginUser(@RequestBody LoginDTO loginDTO) {
        try {
            boolean authenticated = userService.loginUser(loginDTO.getLoginId(), loginDTO.getPassword());
            if (authenticated) {
                return ResponseEntity.ok("로그인 성공");
            } else {
                return ResponseEntity.status(401).body("로그인 실패: 아이디 또는 비밀번호가 틀렸습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}