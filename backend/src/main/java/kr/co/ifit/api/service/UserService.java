package kr.co.ifit.api.service;

import kr.co.ifit.api.request.UserDtoReq;
import kr.co.ifit.db.entity.EmailVerification;
import kr.co.ifit.db.entity.User;
import kr.co.ifit.db.repository.EmailVerificationRepository;
import kr.co.ifit.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailVerificationRepository emailVerificationRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUserDetailService userDetailService;


    // 회원가입
    @Transactional
    public void registerUser(UserDtoReq userDtoReq) {
        if (userRepository.existsByLoginId(userDtoReq.getLoginId())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        // 이메일 검증
        Optional<EmailVerification> optionalEmail = emailVerificationRepository.findByUserEmail(userDtoReq.getEmail());
        //  두 조건이 false일 때 예외가 발생되지 않고 회원가입이 완료됨
        //  optional의 값이 비어 있지 않을 때, 이메일 인증이 true일 때
        if (optionalEmail.isEmpty() || !optionalEmail.get().getEmailVerified()) {
            throw new IllegalArgumentException("이메일이 인증되지 않았거나 존재하지 않습니다.");
        }

        User user = new User(
                userDtoReq.getLoginId(),
                passwordEncoder.encode(userDtoReq.getPassword()),
                userDtoReq.getUsername(),
                userDtoReq.getPhoneNumber(),
                userDtoReq.getEmail()
        );
        userRepository.save(user);
    }

    // 아이디 중복 확인
    public boolean checkIdAvailability(String id) {
        return !userRepository.existsByLoginId(id);
    }


    // 로그인 정보를 검증하는 기능
    public boolean authenticateUser(String loginId, String password) {
        try {
            //  사용자 정보를 로드
            UserDetails userDetails = userDetailService.loadUserByUsername(loginId);

            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                return false;
            }
            //  인증 토큰 생성
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
            //   인증 시도
            Authentication authentication = authenticationManager.authenticate(authToken);
            //  인증 성공 여부 반환
            return authentication.isAuthenticated();
        } catch (AuthenticationException e) {
            return false;
        }
    }
}
//
//    // 로그아웃
//    public void logoutUser(HttpSession session) {
//        session.invalidate();
//    }