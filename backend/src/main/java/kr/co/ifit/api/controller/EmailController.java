package kr.co.ifit.api.controller;

import kr.co.ifit.api.request.UserDtoReq;
import kr.co.ifit.api.service.EmailService;
import kr.co.ifit.api.service.EmailVerificationService;
import kr.co.ifit.common.util.UserContextUtil;
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
    private final UserContextUtil userContextUtil;

    @PostMapping("/sendVerificationCode")
    public ResponseEntity<?> sendVerificationCode(@RequestBody UserDtoReq userDtoReq) {
        String email = userDtoReq.getEmail();
        emailService.sendVerificationEmail(email);
        return ResponseEntity.ok("인증 번호 발송");
    }

    @PostMapping("/verifyEmail")
    public ResponseEntity<?> verifyEmail(@RequestBody UserDtoReq userDtoReq) {
        String email = userDtoReq.getEmail();
        String enteredCode = userDtoReq.getEnteredCode();

        boolean verified = emailVerificationService.verifyEmail(email, enteredCode);
        if (verified) {
            return ResponseEntity.ok().body(Map.of("message", "인증 성공"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "이메일 만료 및 인증 번호가 다릅니다."));
        }
    }

//    =========================================================================================

    // 이메일 변경 시 이메일 인증 발송 및 확인
    @PostMapping("/updateEmail/sendVerificationCode")
    public ResponseEntity<?> sendVerificationCodeForEmailUpdate(@RequestBody UserDtoReq userDtoReq) {
        Long userId = userContextUtil.getAuthenticatedUserId();
        if (userId == null) {
            //  인증되지 않은 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        userDtoReq.setUserId(userId);
//        String newEmail = userDtoReq.getEmail();
        emailService.sendVerificationUpdateEmail(userDtoReq);
        return ResponseEntity.ok().body(Map.of("message", "이메일 변경을 위한 인증번호가 발송되었습니다."));
    }

    @PostMapping("/updateEmail/verifyEmail")
    public ResponseEntity<?> verifyEmailForUpdate(@RequestBody UserDtoReq userDtoReq) {
        Long userId = userContextUtil.getAuthenticatedUserId();
        if (userId == null) {
            //  인증되지 않은 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        userDtoReq.setUserId(userId);

        boolean verified = emailVerificationService.verifyUpdateEmail(userDtoReq);
        if (verified) {
            return ResponseEntity.ok().body(Map.of("message", "인증 성공"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", "인증 실패: 이메일 만료 또는 인증 번호가 다릅니다."));
    }

    @PostMapping("/updateEmail")
    public ResponseEntity<?> updateEmail(@RequestBody UserDtoReq userDtoReq) {
        Long userId = userContextUtil.getAuthenticatedUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        userDtoReq.setUserId(userId);

        boolean updated = emailVerificationService.updateUserEmail(userDtoReq);
        if (updated) {
            return ResponseEntity.ok().body(Map.of("message", "이메일 변경 성공"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "이메일 변경 실패"));
    }
}
