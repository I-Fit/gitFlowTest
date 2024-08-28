package kr.co.ifit.service;

import kr.co.ifit.domain.entity.User;
import kr.co.ifit.repository.UserRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class EmailVerificationService {

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    public EmailVerificationService(UserRepository userRepository, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
    }

    public void createAndSendVerificationCode(String email) {
        String verificationCode = generateVerificationCode();
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(10); // 10분 유효

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setEmailVerificationCode(verificationCode);
            user.setEmailVerificationCodeExpiry(expiryDate);
            userRepository.save(user);

            String subject = "Your Email Verification Code";
            String text = "Your verification code is: " + verificationCode;
            sendEmail(email, subject, text);
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 6자리 코드 생성
        return String.valueOf(code);
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public String verifyEmail(String email, String code) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getEmailVerificationCode().equals(code) &&
                    user.getEmailVerificationCodeExpiry().isAfter(LocalDateTime.now())) {
                user.setEmailVerified(true);
                user.setEmailVerificationCode(null); // 인증 코드 삭제
                user.setEmailVerificationCodeExpiry(null);
                userRepository.save(user);
                return "Email verified successfully.";
            }
        }
        return "Invalid or expired verification code.";
    }
}