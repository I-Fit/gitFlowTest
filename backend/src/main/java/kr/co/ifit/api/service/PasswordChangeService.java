package kr.co.ifit.api.service;

import kr.co.ifit.api.request.PasswordChangeDtoReq;
import kr.co.ifit.db.entity.User;
import kr.co.ifit.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PasswordChangeService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;

    @Transactional
    public boolean changePassword(PasswordChangeDtoReq request) {
        User user = userRepository.findByLoginId(request.getLoginId());
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // 이메일 인증 확인
        if (!emailVerificationService.verifyEmail(request.getEmail(), request.getEnteredCode())) {
            throw new RuntimeException("Email verification failed");
        }

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        // 새 비밀번호 확인
        if (!request.getPassword().equals(request.getPasswordCheck())) {
            throw new RuntimeException("New passwords do not match");
        }

        // 비밀번호 변경
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        return true;
    }
}