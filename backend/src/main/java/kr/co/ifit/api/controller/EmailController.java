package kr.co.ifit.api.controller;

import kr.co.ifit.api.request.EmailDtoReq;
import kr.co.ifit.api.request.UserDtoReq;
import kr.co.ifit.api.service.EmailService;
import kr.co.ifit.api.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private final EmailVerificationService emailVerificationService;

    @PostMapping("/sendVerificationCode")
    public ResponseEntity<?> sendVerificationCode(@RequestBody UserDtoReq userDtoReq) {
        String email = userDtoReq.getEmail();
        emailService.sendVerificationEmail(email);
        return ResponseEntity.ok("인증 번호 발송");
    }

    @PostMapping("/verifyEmail")
    public ResponseEntity<?> verifyEmail(@RequestBody EmailDtoReq emailDtoReq) {
        String email = emailDtoReq.getEmail();
        String enteredCode = emailDtoReq.getEnteredCode();

        boolean verified = emailVerificationService.verifyEmail(email, enteredCode);
        if (verified) {
            return ResponseEntity.ok().body(Map.of("message", "인증 성공"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "이메일 만료 및 인증 번호가 다릅니다."));
        }
    }
}
