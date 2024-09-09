package kr.co.ifit.api.controller;

import kr.co.ifit.api.request.EmailDTO;
import kr.co.ifit.api.request.UserDTO;
import kr.co.ifit.api.service.EmailService;
import kr.co.ifit.api.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private final EmailVerificationService emailVerificationService;

    @PostMapping("/sendVerificationCode")
    public ResponseEntity<?> sendVerificationCode(@RequestBody UserDTO userDTO) {
        String email = userDTO.getEmail();
        emailService.sendVerificationEmail(email);
        return ResponseEntity.ok("인증 번호 발송");
    }

    @PostMapping("/verifyEmail")
    public ResponseEntity<?> verifyEmail(@RequestBody UserDTO userDTO) {
        String email = userDTO.getEmail();
        String enteredCode = userDTO.getEnteredCode();

        boolean verified = emailVerificationService.verifyEmail(email, enteredCode);
        if (verified) {
            return ResponseEntity.ok().body(Map.of("message", "인증 성공"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "이메일 만료 및 인증 번호가 다릅니다."));
        }
    }
}
