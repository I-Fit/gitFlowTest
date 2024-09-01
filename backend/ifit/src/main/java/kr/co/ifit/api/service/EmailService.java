package kr.co.ifit.api.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kr.co.ifit.db.entity.User;
import kr.co.ifit.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    public void sendEmail(String to, String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true); // HTML content

        mailSender.send(message);
    }

    public String verifyEmail(String email, String code) {
        // Optional로 반환된 User를 처리하는 부분
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (!optionalUser.isPresent()) {
            return "사용자를 찾을 수 없습니다.";
        }

        User user = optionalUser.get();

        if (!code.equals(user.getEmailCode())) {
            return "인증 코드가 올바르지 않습니다.";
        }

        if (user.getEmailcodeExpiry().isBefore(LocalDateTime.now())) {
            return "인증 코드가 만료되었습니다.";
        }

        user.setEmailVerified(true);
        user.setEmailCode(null);
        user.setEmailcodeExpiry(null);
        userRepository.save(user);

        return "이메일 인증 성공";
    }
}