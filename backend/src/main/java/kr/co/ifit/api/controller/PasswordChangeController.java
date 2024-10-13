package kr.co.ifit.api.controller;

import kr.co.ifit.api.request.PasswordChangeDtoReq;
import kr.co.ifit.api.request.UserDtoReq;
import kr.co.ifit.api.service.EmailService;
import kr.co.ifit.api.service.EmailVerificationService;
import kr.co.ifit.api.service.PasswordChangeService;
import kr.co.ifit.common.util.UserContextUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/password")
public class PasswordChangeController {

    private final UserContextUtil userContextUtil;
    private final EmailService emailService;
    private final EmailVerificationService emailVerificationService;
    private final PasswordChangeService passwordChangeService;

    @PostMapping("/send-verification-code")
    public ResponseEntity<?> sendVerificationCodeForPasswordChange(@RequestBody UserDtoReq userDtoReq) {
        Long userId = userContextUtil.getAuthenticatedUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        userDtoReq.setUserId(userId);
        emailService.sendVerificationPasswordChangeEmail(userDtoReq);
        return ResponseEntity.ok().body(Map.of("message", "비밀번호 변경을 위한 인증번호가 발송되었습니다."));
    }

    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCodeForPasswordChange(@RequestBody UserDtoReq userDtoReq) {
        Long userId = userContextUtil.getAuthenticatedUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        userDtoReq.setUserId(userId);

        boolean verified = emailVerificationService.verifyPasswordChangeEmail(userDtoReq);
        if (verified) {
            return ResponseEntity.ok().body(Map.of("message", "인증 성공"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", "인증 실패: 인증 번호가 잘못되었거나 만료되었습니다."));
    }

    @PostMapping("/change")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeDtoReq passwordChangeDtoReq) {
        Long userId = userContextUtil.getAuthenticatedUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        passwordChangeDtoReq.setUserId(userId);

        try {
            passwordChangeService.changePassword(passwordChangeDtoReq);
            return ResponseEntity.ok().body(Map.of("message", "비밀번호 변경 성공"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "비밀번호 변경 실패: " + e.getMessage()));
        }
    }
}