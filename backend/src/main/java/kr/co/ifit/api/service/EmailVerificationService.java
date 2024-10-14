package kr.co.ifit.api.service;

import kr.co.ifit.api.request.UserDtoReq;
import kr.co.ifit.db.entity.EmailVerification;
import kr.co.ifit.db.repository.EmailVerificationRepository;
import kr.co.ifit.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class EmailVerificationService {

    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;

    //  이메일과 입력된 인증 코드를 받아 검증, 인증 코드가 맞고, 만료 안됐으면, 사용자의 이메일 인증 상태를 업데이트함
    @Transactional
    public boolean verifyEmail(String email, String enteredCode) {
        // 이메일로 인증 정보를 조회
        Optional<EmailVerification> optionalVerification = emailVerificationRepository.findByUserEmail(email);

        //  인증 정보가 존재하는지 확인
        if (optionalVerification.isPresent()) {
            EmailVerification emailVerification = optionalVerification.get();

            //  인증 코드와 만료 시간 확인
            boolean isCodeValid = emailVerification.getEmailCode().equals(enteredCode);
            boolean isNotExpired = emailVerification.getExpiryTime().isAfter(LocalDateTime.now());

            if (isCodeValid && isNotExpired) {
                // 인증 완료 시 EmailVerification 엔티티의 emailVerified 필드 true로 업데이트
                Optional<EmailVerification> optionalEmail = emailVerificationRepository.findByUserEmail(email);
                optionalEmail.ifPresent(verification -> {
                    verification.setEmailVerified(true);        // 인증 상태 true 저장
                    emailVerificationRepository.save(verification); // 저장
                });
                return true;    //  인증 성공
            }
        }
        return false;       // 인증 정보가 없으면
    }

    @Transactional
    public boolean verifyUpdateEmail(UserDtoReq userDtoReq) {
        String email = userDtoReq.getEmail();
        String enteredCode = userDtoReq.getEnteredCode();

        return verifyEmail(email, enteredCode);
    }

    // 이메일 변경을 위한 메서드
    @Transactional
    public boolean updateUserEmail(UserDtoReq dto) {

        boolean isVerified = emailVerificationRepository.findByUserEmail(dto.getEmail())
                .map(EmailVerification::getEmailVerified).orElse(false);

        if (!isVerified) {
            return false;
        }
        return userRepository.findById(dto.getUserId())
                .map(user -> {
                    user.setEmail(dto.getEmail());    // 새로운 이메일로 변경
                    userRepository.save(user);  // 변경된 정보 저장

                    // 이메일 변경 후, 기존의 이메일 인증 데이터 삭제?
                    emailVerificationRepository.deleteByUserEmail(dto.getEmail());

                    return true;    // 성공적으로 업데이트
                })
                .orElse(false); // 사용자가 존재하지 않으면 false 반환
    }
}