package kr.co.ifit.api.service;

import kr.co.ifit.api.request.PasswordChangeDtoReq;
import kr.co.ifit.db.entity.EmailVerification;
import kr.co.ifit.db.entity.User;
import kr.co.ifit.db.repository.EmailVerificationRepository;
import kr.co.ifit.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordChangeService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;
    private final EmailVerificationRepository emailVerificationRepository;

    @Transactional
    public void changePassword(PasswordChangeDtoReq request) {
        User user = userRepository.findById(request.getUserId()).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        //  현재 비밀번호와 입력한 비밀번호가 일치한지
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("현재 비밀번호가 일치하지 않습니다.");
        }

        if (!request.getPassword().equals(request.getPasswordCheck())) {
            throw new RuntimeException("새 비밀번호가 일치하지 않습니다.");
        }

        // 이메일 인증 확인
        Optional<EmailVerification> emailVerification = emailVerificationRepository.findByUserEmail(request.getEmail());
        if (emailVerification.isEmpty() || !emailVerification.get().getEmailVerified()) {
            throw new RuntimeException("이메일 인증이 필요합니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        emailVerificationRepository.deleteByUserEmail(user.getEmail());
    }
}