package kr.co.ifit.api.service;

import kr.co.ifit.db.entity.Coupon;
import kr.co.ifit.db.entity.EmailVerification;
import kr.co.ifit.db.entity.User;
import kr.co.ifit.db.repository.*;
import kr.co.ifit.api.request.UserDtoReq;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;

import static com.mysql.cj.conf.PropertyKey.logger;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailVerificationRepository emailVerificationRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUserDetailService userDetailService;
    private final CouponService couponService;

    private final UserRepository userRepository;




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
        emailVerificationRepository.deleteByUserEmail(userDtoReq.getEmail());

        //  회원가입 쿠폰 생성
        couponService.addWelcomeCoupon(user);
    }

    // 아이디 중복 확인
    public boolean checkIdAvailability(String id) {
        return userRepository.existsByLoginId(id);
    }

    // 로그인 ID로 사용자 조회
    public User findByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId);
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

    @Transactional
    // 아이디 찾기 - email로 사용자 정보 찾아서 loginId 반환
    public String findUserIdByEmail(String email) {
        User user = userRepository.findByEmail(email);
        emailVerificationRepository.deleteByUserEmail(email);
        return user != null ? user.getLoginId() : null;
    }

    @Transactional
    //  비밀번호 재설정 - email로 사용자 정보 찾아서 password 업데이트
    public void updatePassword(String loginId, String password) {
        User user = userRepository.findByLoginId(loginId);

        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        userRepository.save(user);
        emailVerificationRepository.deleteByUserEmail(user.getEmail());
    }
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    //  회원탈퇴 시 user와 관련된 엔티티 모두 삭제
    @Transactional
    public boolean deleteUserAndRelatedData(Long userId) {
        try {
            userRepository.deleteById(userId);
            return true;
        } catch (Exception e) {
            logger.error("User deletion failed: {}", e.getMessage());
            throw e;
//            return false;
        }
    }
}