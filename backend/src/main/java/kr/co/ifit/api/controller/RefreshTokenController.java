package kr.co.ifit.api.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RefreshTokenController {

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshAccessToken(@RequestHeader("refresh-token") String refreshToken) {
        try {
            return ResponseEntity.status(HttpServletResponse.SC_OK).body("Processing request through filter");
        } catch (Exception e) {
            return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body("서버와 통신 실패");
        }
    }

    @DeleteMapping("/tokens/user/{userId}")
    public ResponseEntity<String> invalidRefreshToken(@PathVariable Long userId) {
        return ResponseEntity.ok("인증 만료, 재로그인 해주세요.");
    }
}
