package kr.co.ifit.api.service;

import kr.co.ifit.db.entity.EmailVerification;
import kr.co.ifit.db.repository.EmailVerificationRepository;
import kr.co.ifit.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
//  이메일 인증 번호를 전송하는 메서드 구현
public class EmailService {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;

    //  이메일 주소를 받아 인증 코드를 생성하고 이메일로 발송
    @Transactional
    public void sendVerificationEmail(String email) {
        //  인증번호 생성
        String verificationCode = generateVerificationCode();

        //  인증번호 내용 설정
        String subject = "[ifit] 메일 인증";
        String text = "인증 번호는 : " + verificationCode;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);

        //  인증번호 전송
        mailSender.send(message);

        // 이메일로 인증 번호를 보내고, 새로운 emailVerification을 만들어서 값들을 저장
        EmailVerification emailVerification = new EmailVerification();
        emailVerification.setUserEmail(email);      // 사용자가 입력한 이메일
        emailVerification.setEmailCode(verificationCode);       // 서버가 만든 인증 코드
        emailVerification.setExpiryTime(LocalDateTime.now().plusMinutes(5));        // 인증 번호 만료 시간

        emailVerificationRepository.save(emailVerification);
    }

    //  6자리 인증 코드 생성
    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}
